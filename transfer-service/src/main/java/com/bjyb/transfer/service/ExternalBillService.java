package com.bjyb.transfer.service;

import com.bjyb.common.dto.ExternalBillDTO;
import com.bjyb.common.dto.ExternalBillImportDTO;
import com.bjyb.common.dto.Result;
import com.bjyb.common.entity.ExternalReimbursementBill;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.ExternalReimbursementBillMapper;
import com.bjyb.common.utils.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalBillService {

    private static final Logger log = LoggerFactory.getLogger(ExternalBillService.class);

    @Autowired
    private ExternalReimbursementBillMapper billMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> importBills(ExternalBillImportDTO request) {
        log.info("开始导入外省报销单据，来源系统: {}, 单据数量: {}", request.getSourceSystem(), request.getBills() != null ? request.getBills().size() : 0);

        if (request.getBills() == null || request.getBills().isEmpty()) {
            throw new BusinessException("导入单据不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();

        for (ExternalBillDTO billDTO : request.getBills()) {
            try {
                validateBill(billDTO);

                ExternalReimbursementBill existingBill = billMapper.selectByExternalBillNo(billDTO.getExternalBillNo());
                if (existingBill != null) {
                    failCount++;
                    failMessages.add("单据[" + billDTO.getExternalBillNo() + "]已存在，跳过");
                    continue;
                }

                ExternalReimbursementBill bill = convertToEntity(billDTO);
                bill.setSourceSystem(request.getSourceSystem());
                bill.setImportTime(LocalDateTime.now());
                bill.setBillStatus("IMPORTED");
                bill.setRemark("批量导入");

                billMapper.insert(bill);
                successCount++;
            } catch (Exception e) {
                failCount++;
                String errorMsg = "单据[" + billDTO.getExternalBillNo() + "]导入失败: " + e.getMessage();
                failMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }

        String resultMsg = String.format("导入完成，成功: %d条，失败: %d条", successCount, failCount);
        if (!failMessages.isEmpty()) {
            resultMsg += "，失败原因: " + String.join("; ", failMessages.subList(0, Math.min(5, failMessages.size())));
        }

        log.info(resultMsg);
        return Result.success(resultMsg, successCount + "/" + (successCount + failCount));
    }

    private void validateBill(ExternalBillDTO billDTO) {
        if (billDTO.getExternalBillNo() == null || billDTO.getExternalBillNo().trim().isEmpty()) {
            throw new BusinessException("外省单据号不能为空");
        }
        if (billDTO.getHospitalCode() == null || billDTO.getHospitalCode().trim().isEmpty()) {
            throw new BusinessException("医院编码不能为空");
        }
        if (billDTO.getIdCard() == null || billDTO.getIdCard().trim().isEmpty()) {
            throw new BusinessException("身份证号不能为空");
        }
        if (billDTO.getTreatmentDate() == null) {
            throw new BusinessException("就医日期不能为空");
        }
        if (BigDecimalUtils.isLessThan(billDTO.getTotalAmount(), BigDecimalUtils.defaultIfNull(billDTO.getReimbursementAmount()))) {
            throw new BusinessException("总金额不能小于报销金额");
        }
    }

    private ExternalReimbursementBill convertToEntity(ExternalBillDTO dto) {
        ExternalReimbursementBill bill = new ExternalReimbursementBill();
        bill.setExternalBillNo(dto.getExternalBillNo());
        bill.setHospitalCode(dto.getHospitalCode());
        bill.setHospitalName(dto.getHospitalName());
        bill.setProvinceCode(dto.getProvinceCode());
        bill.setProvinceName(dto.getProvinceName());
        bill.setIdCard(dto.getIdCard());
        bill.setName(dto.getName());
        bill.setMedicalType(dto.getMedicalType());
        bill.setTotalAmount(BigDecimalUtils.defaultIfNull(dto.getTotalAmount()));
        bill.setReimbursementAmount(BigDecimalUtils.defaultIfNull(dto.getReimbursementAmount()));
        bill.setPersonalPayAmount(BigDecimalUtils.defaultIfNull(dto.getPersonalPayAmount()));
        bill.setAccountPayAmount(BigDecimalUtils.defaultIfNull(dto.getAccountPayAmount()));
        bill.setCashPayAmount(BigDecimalUtils.defaultIfNull(dto.getCashPayAmount()));
        bill.setTreatmentDate(dto.getTreatmentDate());
        bill.setDischargeDate(dto.getDischargeDate());
        bill.setDiagnosis(dto.getDiagnosis());
        bill.setInvoiceNo(dto.getInvoiceNo());
        return bill;
    }

    public Result<String> updateBillStatus(String externalBillNo, String billStatus, String batchNo) {
        ExternalReimbursementBill bill = billMapper.selectByExternalBillNo(externalBillNo);
        if (bill == null) {
            throw new BusinessException("未找到单据，单据号: " + externalBillNo);
        }

        int count = billMapper.updateBillStatus(externalBillNo, billStatus, batchNo, LocalDateTime.now());
        if (count > 0) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
}
