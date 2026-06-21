package com.bjyb.transfer.service;

import com.bjyb.common.dto.ReconciliationRequestDTO;
import com.bjyb.common.dto.ReconciliationResultDTO;
import com.bjyb.common.entity.*;
import com.bjyb.common.enums.ExceptionStatusEnum;
import com.bjyb.common.enums.ExceptionTypeEnum;
import com.bjyb.common.enums.MatchStatusEnum;
import com.bjyb.common.enums.ReconciliationStatusEnum;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.*;
import com.bjyb.common.utils.BigDecimalUtils;
import com.bjyb.common.utils.BusinessNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);

    private static final BigDecimal DIFF_THRESHOLD = new BigDecimal("0.01");

    @Autowired
    private ExternalReimbursementBillMapper externalBillMapper;

    @Autowired
    private SettlementRecordMapper settlementRecordMapper;

    @Autowired
    private ReconciliationBatchMapper batchMapper;

    @Autowired
    private ReconciliationDetailMapper detailMapper;

    @Autowired
    private ReconciliationExceptionMapper exceptionMapper;

    @Transactional(rollbackFor = Exception.class)
    public ReconciliationResultDTO executeReconciliation(ReconciliationRequestDTO request) {
        LocalDate reconciliationDate = request.getReconciliationDate() != null ? request.getReconciliationDate() : LocalDate.now().minusDays(1);
        String provinceCode = request.getProvinceCode();

        log.info("开始执行对账，对账日期: {}, 省份: {}", reconciliationDate, provinceCode);

        ReconciliationBatch batch = createBatch(reconciliationDate, provinceCode, request.getProvinceName(), request.getOperator());

        try {
            List<ExternalReimbursementBill> externalBills = externalBillMapper.selectByDateAndProvince(
                    reconciliationDate, reconciliationDate, provinceCode, "IMPORTED");

            List<SettlementRecord> localSettlements = settlementRecordMapper.selectByDateAndProvince(
                    reconciliationDate.atStartOfDay(), reconciliationDate.atTime(23, 59, 59), provinceCode);

            log.info("查询到外省单据: {}条，本地结算记录: {}条", externalBills.size(), localSettlements.size());

            batch.setTotalBills(externalBills.size());
            batch.setTotalExternalAmount(calculateTotalAmount(externalBills));
            batch.setTotalLocalAmount(calculateLocalTotalAmount(localSettlements));

            Map<String, SettlementRecord> localRecordMap = buildLocalRecordMap(localSettlements);
            Set<String> matchedLocalNos = new HashSet<>();

            int matched = 0;
            int mismatched = 0;
            int missingLocal = 0;
            int missingExternal = 0;
            int exceptionCount = 0;

            for (ExternalReimbursementBill bill : externalBills) {
                SettlementRecord localRecord = findMatchingLocalRecord(bill, localRecordMap);

                if (localRecord != null) {
                    matchedLocalNos.add(localRecord.getSettlementNo());
                    ReconciliationDetail detail = reconcileSingleBill(bill, localRecord, batch.getBatchNo());

                    if (MatchStatusEnum.MATCHED.getCode().equals(detail.getMatchStatus())) {
                        matched++;
                    } else {
                        mismatched++;
                        exceptionCount++;
                        createExceptionRecord(detail, bill, localRecord, ExceptionTypeEnum.AMOUNT_DIFF);
                    }

                    detailMapper.insert(detail);
                    updateBillStatus(bill, batch.getBatchNo());
                } else {
                    missingLocal++;
                    exceptionCount++;
                    ReconciliationDetail detail = createMissingDetail(bill, null, batch.getBatchNo(), MatchStatusEnum.MISSING_LOCAL);
                    detailMapper.insert(detail);
                    createExceptionRecord(detail, bill, null, ExceptionTypeEnum.MISSING_LOCAL);
                    updateBillStatusException(bill, batch.getBatchNo());
                }
            }

            for (SettlementRecord localRecord : localSettlements) {
                if (!matchedLocalNos.contains(localRecord.getSettlementNo())) {
                    missingExternal++;
                    exceptionCount++;
                    ReconciliationDetail detail = createMissingDetail(null, localRecord, batch.getBatchNo(), MatchStatusEnum.MISSING_EXTERNAL);
                    detailMapper.insert(detail);
                    createExceptionRecord(detail, null, localRecord, ExceptionTypeEnum.MISSING_EXTERNAL);
                }
            }

            batch.setMatchedBills(matched);
            batch.setMismatchedBills(mismatched);
            batch.setMissingLocalBills(missingLocal);
            batch.setMissingExternalBills(missingExternal);
            batch.setDiffAmount(BigDecimalUtils.safeSubtract(batch.getTotalExternalAmount(), batch.getTotalLocalAmount()));
            batch.setBatchStatus(ReconciliationStatusEnum.COMPLETED.getCode());
            batch.setEndTime(LocalDateTime.now());

            batchMapper.updateById(batch);

            log.info("对账完成，批次号: {}, 一致: {}, 不一致: {}, 本地缺失: {}, 外省缺失: {}, 异常数: {}",
                    batch.getBatchNo(), matched, mismatched, missingLocal, missingExternal, exceptionCount);

            return buildResultDTO(batch, exceptionCount);

        } catch (Exception e) {
            log.error("对账执行失败，批次号: {}", batch.getBatchNo(), e);
            batch.setBatchStatus(ReconciliationStatusEnum.FAILED.getCode());
            batch.setEndTime(LocalDateTime.now());
            batch.setRemark("对账失败: " + e.getMessage());
            batchMapper.updateById(batch);
            throw new BusinessException("对账执行失败: " + e.getMessage());
        }
    }

    private ReconciliationBatch createBatch(LocalDate reconciliationDate, String provinceCode, String provinceName, String operator) {
        ReconciliationBatch batch = new ReconciliationBatch();
        batch.setBatchNo(BusinessNoGenerator.generateBatchNo());
        batch.setReconciliationDate(reconciliationDate);
        batch.setProvinceCode(provinceCode);
        batch.setProvinceName(provinceName);
        batch.setTotalBills(0);
        batch.setMatchedBills(0);
        batch.setMismatchedBills(0);
        batch.setMissingLocalBills(0);
        batch.setMissingExternalBills(0);
        batch.setTotalLocalAmount(BigDecimal.ZERO);
        batch.setTotalExternalAmount(BigDecimal.ZERO);
        batch.setDiffAmount(BigDecimal.ZERO);
        batch.setBatchStatus(ReconciliationStatusEnum.PROCESSING.getCode());
        batch.setStartTime(LocalDateTime.now());
        batch.setOperator(operator != null ? operator : "system");
        batchMapper.insert(batch);
        log.info("创建对账批次: {}", batch.getBatchNo());
        return batch;
    }

    private Map<String, SettlementRecord> buildLocalRecordMap(List<SettlementRecord> records) {
        Map<String, SettlementRecord> map = new HashMap<>();
        for (SettlementRecord record : records) {
            String key = buildMatchKey(record.getIdCard(), record.getMedicalType(), record.getSettlementTime() != null ? record.getSettlementTime().toLocalDate() : null);
            map.put(key, record);
        }
        return map;
    }

    private String buildMatchKey(String idCard, String medicalType, LocalDate date) {
        return idCard + "|" + (medicalType != null ? medicalType : "") + "|" + (date != null ? date.toString() : "");
    }

    private SettlementRecord findMatchingLocalRecord(ExternalReimbursementBill bill, Map<String, SettlementRecord> localMap) {
        String key = buildMatchKey(bill.getIdCard(), bill.getMedicalType(), bill.getTreatmentDate());
        return localMap.get(key);
    }

    private ReconciliationDetail reconcileSingleBill(ExternalReimbursementBill bill, SettlementRecord local, String batchNo) {
        ReconciliationDetail detail = new ReconciliationDetail();
        detail.setBatchNo(batchNo);
        detail.setExternalBillNo(bill.getExternalBillNo());
        detail.setLocalSettlementNo(local.getSettlementNo());
        detail.setHospitalCode(bill.getHospitalCode());
        detail.setIdCard(bill.getIdCard());
        detail.setName(bill.getName());
        detail.setMedicalType(bill.getMedicalType());
        detail.setTreatmentDate(bill.getTreatmentDate());

        BigDecimal localTotal = BigDecimalUtils.defaultIfNull(local.getTotalAmount());
        BigDecimal localReimbursement = BigDecimalUtils.defaultIfNull(local.getReimbursementAmount());
        BigDecimal localPersonalPay = BigDecimalUtils.defaultIfNull(local.getPersonalAccountPay());

        BigDecimal externalTotal = BigDecimalUtils.defaultIfNull(bill.getTotalAmount());
        BigDecimal externalReimbursement = BigDecimalUtils.defaultIfNull(bill.getReimbursementAmount());
        BigDecimal externalPersonalPay = BigDecimalUtils.defaultIfNull(bill.getPersonalPayAmount());

        detail.setLocalTotalAmount(localTotal);
        detail.setLocalReimbursementAmount(localReimbursement);
        detail.setLocalPersonalPay(localPersonalPay);
        detail.setExternalTotalAmount(externalTotal);
        detail.setExternalReimbursementAmount(externalReimbursement);
        detail.setExternalPersonalPay(externalPersonalPay);

        BigDecimal totalDiff = BigDecimalUtils.safeSubtract(externalTotal, localTotal);
        BigDecimal reimbursementDiff = BigDecimalUtils.safeSubtract(externalReimbursement, localReimbursement);
        BigDecimal personalPayDiff = BigDecimalUtils.safeSubtract(externalPersonalPay, localPersonalPay);

        detail.setTotalDiff(totalDiff);
        detail.setReimbursementDiff(reimbursementDiff);
        detail.setPersonalPayDiff(personalPayDiff);

        boolean totalMatch = totalDiff.abs().compareTo(DIFF_THRESHOLD) <= 0;
        boolean reimbursementMatch = reimbursementDiff.abs().compareTo(DIFF_THRESHOLD) <= 0;
        boolean personalPayMatch = personalPayDiff.abs().compareTo(DIFF_THRESHOLD) <= 0;

        if (totalMatch && reimbursementMatch && personalPayMatch) {
            detail.setMatchStatus(MatchStatusEnum.MATCHED.getCode());
            detail.setReconciliationResult("对账一致");
            detail.setIsException(0);
        } else {
            detail.setMatchStatus(MatchStatusEnum.MISMATCHED.getCode());
            detail.setReconciliationResult(buildMismatchReason(totalDiff, reimbursementDiff, personalPayDiff));
            detail.setIsException(1);
        }

        detail.setExceptionHandled(0);
        return detail;
    }

    private String buildMismatchReason(BigDecimal totalDiff, BigDecimal reimbursementDiff, BigDecimal personalPayDiff) {
        List<String> reasons = new ArrayList<>();
        if (totalDiff.abs().compareTo(DIFF_THRESHOLD) > 0) {
            reasons.add("总金额差额: " + totalDiff);
        }
        if (reimbursementDiff.abs().compareTo(DIFF_THRESHOLD) > 0) {
            reasons.add("报销金额差额: " + reimbursementDiff);
        }
        if (personalPayDiff.abs().compareTo(DIFF_THRESHOLD) > 0) {
            reasons.add("个人支付差额: " + personalPayDiff);
        }
        return String.join("; ", reasons);
    }

    private ReconciliationDetail createMissingDetail(ExternalReimbursementBill bill, SettlementRecord local,
                                                      String batchNo, MatchStatusEnum status) {
        ReconciliationDetail detail = new ReconciliationDetail();
        detail.setBatchNo(batchNo);
        detail.setIsException(1);
        detail.setExceptionHandled(0);
        detail.setMatchStatus(status.getCode());

        if (bill != null) {
            detail.setExternalBillNo(bill.getExternalBillNo());
            detail.setHospitalCode(bill.getHospitalCode());
            detail.setIdCard(bill.getIdCard());
            detail.setName(bill.getName());
            detail.setMedicalType(bill.getMedicalType());
            detail.setTreatmentDate(bill.getTreatmentDate());
            detail.setExternalTotalAmount(BigDecimalUtils.defaultIfNull(bill.getTotalAmount()));
            detail.setExternalReimbursementAmount(BigDecimalUtils.defaultIfNull(bill.getReimbursementAmount()));
            detail.setExternalPersonalPay(BigDecimalUtils.defaultIfNull(bill.getPersonalPayAmount()));
            detail.setReconciliationResult(status == MatchStatusEnum.MISSING_LOCAL ? "本地缺失对应结算记录" : "外省缺失对应单据");
        }

        if (local != null) {
            detail.setLocalSettlementNo(local.getSettlementNo());
            detail.setHospitalCode(local.getHospitalCode());
            detail.setIdCard(local.getIdCard());
            detail.setName(local.getName());
            detail.setMedicalType(local.getMedicalType());
            detail.setTreatmentDate(local.getSettlementTime() != null ? local.getSettlementTime().toLocalDate() : null);
            detail.setLocalTotalAmount(BigDecimalUtils.defaultIfNull(local.getTotalAmount()));
            detail.setLocalReimbursementAmount(BigDecimalUtils.defaultIfNull(local.getReimbursementAmount()));
            detail.setLocalPersonalPay(BigDecimalUtils.defaultIfNull(local.getPersonalAccountPay()));
            detail.setReconciliationResult("外省缺失对应报销单据");
        }

        return detail;
    }

    private void createExceptionRecord(ReconciliationDetail detail, ExternalReimbursementBill bill,
                                       SettlementRecord local, ExceptionTypeEnum exceptionType) {
        ReconciliationException exception = new ReconciliationException();
        exception.setExceptionNo(BusinessNoGenerator.generateExceptionNo());
        exception.setBatchNo(detail.getBatchNo());
        exception.setReconciliationDetailId(detail.getId());
        exception.setExternalBillNo(detail.getExternalBillNo());
        exception.setLocalSettlementNo(detail.getLocalSettlementNo());
        exception.setHospitalCode(detail.getHospitalCode());
        exception.setIdCard(detail.getIdCard());
        exception.setName(detail.getName());
        exception.setMedicalType(detail.getMedicalType());
        exception.setTreatmentDate(detail.getTreatmentDate());
        exception.setExceptionType(exceptionType.getCode());
        exception.setLocalTotalAmount(detail.getLocalTotalAmount());
        exception.setLocalReimbursementAmount(detail.getLocalReimbursementAmount());
        exception.setExternalTotalAmount(detail.getExternalTotalAmount());
        exception.setExternalReimbursementAmount(detail.getExternalReimbursementAmount());
        exception.setTotalDiff(detail.getTotalDiff());
        exception.setReimbursementDiff(detail.getReimbursementDiff());
        exception.setExceptionDesc(detail.getReconciliationResult());
        exception.setExceptionStatus(ExceptionStatusEnum.PENDING.getCode());

        if (bill != null) {
            exception.setHospitalName(bill.getHospitalName());
            exception.setProvinceCode(bill.getProvinceCode());
            exception.setProvinceName(bill.getProvinceName());
        }

        exceptionMapper.insert(exception);
        log.info("创建对账异常记录: {}, 类型: {}", exception.getExceptionNo(), exceptionType.getDesc());
    }

    private void updateBillStatus(ExternalReimbursementBill bill, String batchNo) {
        externalBillMapper.updateBillStatus(bill.getExternalBillNo(), "RECONCILED", batchNo, LocalDateTime.now());
    }

    private void updateBillStatusException(ExternalReimbursementBill bill, String batchNo) {
        externalBillMapper.updateBillStatus(bill.getExternalBillNo(), "EXCEPTION", batchNo, LocalDateTime.now());
    }

    private BigDecimal calculateTotalAmount(List<ExternalReimbursementBill> bills) {
        BigDecimal total = BigDecimal.ZERO;
        for (ExternalReimbursementBill bill : bills) {
            total = BigDecimalUtils.safeAdd(total, bill.getTotalAmount());
        }
        return total;
    }

    private BigDecimal calculateLocalTotalAmount(List<SettlementRecord> records) {
        BigDecimal total = BigDecimal.ZERO;
        for (SettlementRecord record : records) {
            total = BigDecimalUtils.safeAdd(total, record.getTotalAmount());
        }
        return total;
    }

    private ReconciliationResultDTO buildResultDTO(ReconciliationBatch batch, int exceptionCount) {
        ReconciliationResultDTO dto = new ReconciliationResultDTO();
        dto.setBatchNo(batch.getBatchNo());
        dto.setReconciliationDate(batch.getReconciliationDate());
        dto.setTotalBills(batch.getTotalBills());
        dto.setMatchedBills(batch.getMatchedBills());
        dto.setMismatchedBills(batch.getMismatchedBills());
        dto.setMissingLocalBills(batch.getMissingLocalBills());
        dto.setMissingExternalBills(batch.getMissingExternalBills());
        dto.setTotalLocalAmount(batch.getTotalLocalAmount());
        dto.setTotalExternalAmount(batch.getTotalExternalAmount());
        dto.setDiffAmount(batch.getDiffAmount());
        dto.setBatchStatus(batch.getBatchStatus());
        dto.setExceptionCount(exceptionCount);
        return dto;
    }
}
