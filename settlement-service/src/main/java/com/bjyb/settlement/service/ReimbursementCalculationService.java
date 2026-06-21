package com.bjyb.settlement.service;

import com.bjyb.common.dto.SettlementRequestDTO;
import com.bjyb.common.dto.SettlementResponseDTO;
import com.bjyb.common.dto.TreatmentItemDTO;
import com.bjyb.common.entity.*;
import com.bjyb.common.enums.FlowTypeEnum;
import com.bjyb.common.enums.SettlementStatusEnum;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.*;
import com.bjyb.common.utils.BigDecimalUtils;
import com.bjyb.common.utils.BusinessNoGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ReimbursementCalculationService {

    private static final Logger log = LoggerFactory.getLogger(ReimbursementCalculationService.class);

    @Autowired
    private InsuredPersonMapper insuredPersonMapper;

    @Autowired
    private PersonalAccountMapper personalAccountMapper;

    @Autowired
    private MedicalPolicyMapper medicalPolicyMapper;

    @Autowired
    private CrossProvinceHospitalMapper hospitalMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private SettlementRecordMapper settlementRecordMapper;

    @Autowired
    private AccountFlowRecordMapper flowRecordMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public SettlementResponseDTO calculateAndSettle(SettlementRequestDTO request) {
        log.info("开始处理异地结算请求，请求号: {}, 身份证号: {}", request.getRequestNo(), request.getIdCard());

        InsuredPerson insuredPerson = validateInsuredPerson(request.getIdCard());
        validateHospital(request.getHospitalCode());
        MedicalPolicy policy = findEffectivePolicy(insuredPerson.getInsuranceType(), request.getMedicalType());
        PersonalAccount account = personalAccountMapper.selectByIdCard(request.getIdCard());

        if (account == null) {
            log.info("参保人{}尚未开立个人账户，按余额0处理", insuredPerson.getName());
            account = createDefaultAccount(insuredPerson);
        }

        if (!"NORMAL".equals(account.getAccountStatus())) {
            throw new BusinessException("个人账户状态异常，无法结算");
        }

        BigDecimal accountBalanceBefore = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());
        log.info("参保人: {}, 医保类型: {}, 账户可用余额: {}", insuredPerson.getName(), insuredPerson.getInsuranceType(), accountBalanceBefore);

        String treatmentType = convertMedicalType(request.getMedicalType());
        MedicalRecord medicalRecord = createMedicalRecord(request, insuredPerson);
        SettlementRecord settlement = calculateReimbursement(request, insuredPerson, policy, medicalRecord);

        BigDecimal personalPayAmount = BigDecimalUtils.defaultIfNull(settlement.getPersonalAccountPay());
        if (BigDecimalUtils.isGreaterThan(personalPayAmount, BigDecimal.ZERO)) {
            if (BigDecimalUtils.isLessThan(accountBalanceBefore, personalPayAmount)) {
                BigDecimal cashPay = BigDecimalUtils.safeSubtract(personalPayAmount, accountBalanceBefore);
                settlement.setPersonalAccountPay(accountBalanceBefore);
                settlement.setCashPay(cashPay);
                personalPayAmount = accountBalanceBefore;
                log.info("个账余额不足，个账支付: {}, 现金支付: {}", accountBalanceBefore, cashPay);
            }

            if (BigDecimalUtils.isGreaterThan(personalPayAmount, BigDecimal.ZERO)) {
                int updateCount = personalAccountMapper.deductBalance(account.getId(), personalPayAmount);
                if (updateCount == 0) {
                    throw new BusinessException("个人账户扣款失败，余额不足或状态异常");
                }

                account = personalAccountMapper.selectById(account.getId());
                if (account == null) {
                    throw new BusinessException("扣款后查询账户信息失败");
                }
                createAccountFlow(account, personalPayAmount, FlowTypeEnum.EXPENSE, "结算支付", settlement.getSettlementNo(), request.getHospitalCode());
            }
        }

        settlement.setSettlementStatus(SettlementStatusEnum.SUCCESS.getCode());
        settlement.setSettlementTime(LocalDateTime.now());
        settlementRecordMapper.insert(settlement);

        medicalRecord.setRecordStatus("SETTLED");
        medicalRecordMapper.updateById(medicalRecord);

        BigDecimal accountBalanceAfter = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());
        SettlementResponseDTO response = buildResponse(request, settlement, policy, accountBalanceBefore, accountBalanceAfter);

        log.info("异地结算完成，结算单号: {}, 报销金额: {}, 个账支付: {}", settlement.getSettlementNo(), settlement.getReimbursementAmount(), settlement.getPersonalAccountPay());
        return response;
    }

    private InsuredPerson validateInsuredPerson(String idCard) {
        InsuredPerson insuredPerson = insuredPersonMapper.selectByIdCard(idCard);
        if (insuredPerson == null) {
            throw new BusinessException("未找到参保人信息，身份证号: " + idCard);
        }
        if (!"正常参保".equals(insuredPerson.getInsuranceStatus())) {
            throw new BusinessException("参保人状态异常，当前状态: " + insuredPerson.getInsuranceStatus());
        }
        return insuredPerson;
    }

    private void validateHospital(String hospitalCode) {
        CrossProvinceHospital hospital = hospitalMapper.selectByHospitalCode(hospitalCode);
        if (hospital == null) {
            throw new BusinessException("未找到定点医院信息，医院编码: " + hospitalCode);
        }
        if (!"ACTIVE".equals(hospital.getSettlementStatus())) {
            throw new BusinessException("该医院当前不支持跨省结算，医院: " + hospital.getHospitalName());
        }
    }

    private MedicalPolicy findEffectivePolicy(String insuranceType, String medicalType) {
        String treatmentType = convertMedicalType(medicalType);
        MedicalPolicy policy = medicalPolicyMapper.selectEffectivePolicy(insuranceType, treatmentType);
        if (policy == null) {
            throw new BusinessException("未找到适用的医保报销政策");
        }
        log.info("适用政策: {}, 起付线: {}, 报销比例: {}%, 年度最高支付: {}", policy.getPolicyName(), policy.getDeductible(), policy.getReimbursementRatio(), policy.getMaxReimbursementAmount());
        return policy;
    }

    private String convertMedicalType(String medicalType) {
        if ("门诊".equals(medicalType) || "急诊".equals(medicalType) || "购药".equals(medicalType)) {
            return "门诊";
        } else if ("住院".equals(medicalType)) {
            return "住院";
        }
        return medicalType;
    }

    private MedicalRecord createMedicalRecord(SettlementRequestDTO request, InsuredPerson insuredPerson) {
        CrossProvinceHospital hospital = hospitalMapper.selectByHospitalCode(request.getHospitalCode());

        MedicalRecord record = new MedicalRecord();
        record.setRecordNo(BusinessNoGenerator.generateRecordNo());
        record.setIdCard(request.getIdCard());
        record.setName(insuredPerson.getName());
        record.setHospitalCode(request.getHospitalCode());
        record.setHospitalName(hospital != null ? hospital.getHospitalName() : request.getHospitalName());
        record.setProvinceCode(hospital != null ? hospital.getProvinceCode() : "");
        record.setProvinceName(hospital != null ? hospital.getProvinceName() : "");
        record.setMedicalType(request.getMedicalType());
        record.setAdmissionDate(request.getAdmissionDate());
        record.setDischargeDate(request.getDischargeDate());
        record.setTotalAmount(request.getTotalAmount());
        record.setDiagnosis(request.getDiagnosis());
        record.setInvoiceNo(request.getInvoiceNo());
        record.setReportTime(LocalDateTime.now());
        record.setRecordStatus("PENDING");

        if (request.getTreatmentItems() != null && !request.getTreatmentItems().isEmpty()) {
            try {
                record.setTreatmentItems(objectMapper.writeValueAsString(request.getTreatmentItems()));
            } catch (JsonProcessingException e) {
                log.warn("诊疗项目序列化失败", e);
            }
        }

        medicalRecordMapper.insert(record);
        return record;
    }

    private SettlementRecord calculateReimbursement(SettlementRequestDTO request, InsuredPerson insuredPerson,
                                                    MedicalPolicy policy, MedicalRecord medicalRecord) {
        CrossProvinceHospital hospital = hospitalMapper.selectByHospitalCode(request.getHospitalCode());
        BigDecimal totalAmount = BigDecimalUtils.defaultIfNull(request.getTotalAmount());
        BigDecimal deductible = BigDecimalUtils.defaultIfNull(policy.getDeductible());
        BigDecimal ratio = BigDecimalUtils.safeDivide(policy.getReimbursementRatio(), new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal maxAmount = BigDecimalUtils.defaultIfNull(policy.getMaxReimbursementAmount());

        LocalDateTime yearStart = LocalDateTime.of(LocalDate.now().withMonth(1).withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime yearEnd = LocalDateTime.of(LocalDate.now().withMonth(12).withDayOfMonth(31), LocalTime.MAX);
        BigDecimal yearReimbursed = BigDecimalUtils.defaultIfNull(settlementRecordMapper.sumReimbursementByPolicyAndYear(
                insuredPerson.getIdCard(), policy.getPolicyCode(), yearStart, yearEnd));
        BigDecimal remainingQuota = BigDecimalUtils.safeSubtract(maxAmount, yearReimbursed);

        BigDecimal selfPayAmount = calculateSelfPayAmount(request);
        BigDecimal withinScopeAmount = BigDecimalUtils.safeSubtract(totalAmount, selfPayAmount);
        if (BigDecimalUtils.isNegative(withinScopeAmount)) {
            withinScopeAmount = BigDecimal.ZERO;
        }

        BigDecimal deductibleAmount = BigDecimal.ZERO;
        if (BigDecimalUtils.isGreaterThan(withinScopeAmount, deductible)) {
            deductibleAmount = deductible;
        } else {
            deductibleAmount = withinScopeAmount;
        }

        BigDecimal reimbursementBase = BigDecimalUtils.safeSubtract(withinScopeAmount, deductibleAmount);
        BigDecimal reimbursementAmount = reimbursementBase.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
        reimbursementAmount = BigDecimalUtils.defaultIfNull(reimbursementAmount, BigDecimal.ZERO);

        if (BigDecimalUtils.isGreaterThan(reimbursementAmount, remainingQuota)) {
            reimbursementAmount = remainingQuota;
            log.info("超出年度最高支付限额，按限额报销: {}", reimbursementAmount);
        }

        if (BigDecimalUtils.isNegative(reimbursementAmount)) {
            reimbursementAmount = BigDecimal.ZERO;
        }

        BigDecimal personalAccountPay = BigDecimalUtils.safeSubtract(totalAmount, reimbursementAmount);
        if (BigDecimalUtils.isNegative(personalAccountPay)) {
            personalAccountPay = BigDecimal.ZERO;
        }

        SettlementRecord settlement = new SettlementRecord();
        settlement.setSettlementNo(BusinessNoGenerator.generateSettlementNo());
        settlement.setRecordNo(medicalRecord.getRecordNo());
        settlement.setIdCard(request.getIdCard());
        settlement.setName(insuredPerson.getName());
        settlement.setHospitalCode(request.getHospitalCode());
        settlement.setHospitalName(hospital != null ? hospital.getHospitalName() : request.getHospitalName());
        settlement.setProvinceCode(hospital != null ? hospital.getProvinceCode() : "");
        settlement.setProvinceName(hospital != null ? hospital.getProvinceName() : "");
        settlement.setPolicyCode(policy.getPolicyCode());
        settlement.setMedicalType(request.getMedicalType());
        settlement.setTotalAmount(totalAmount);
        settlement.setSelfPayAmount(selfPayAmount);
        settlement.setDeductibleAmount(deductibleAmount);
        settlement.setWithinScopeAmount(withinScopeAmount);
        settlement.setReimbursementAmount(reimbursementAmount);
        settlement.setPersonalAccountPay(personalAccountPay);
        settlement.setUnifiedFundPay(reimbursementAmount);
        settlement.setOtherFundPay(BigDecimal.ZERO);
        settlement.setCashPay(BigDecimal.ZERO);
        settlement.setSettlementType("NORMAL");
        settlement.setSettlementStatus(SettlementStatusEnum.PENDING.getCode());
        settlement.setOperator("system");
        settlement.setRemark("跨省异地就医直接结算");

        log.info("报销计算完成 - 总费用: {}, 自费: {}, 起付线: {}, 范围内: {}, 报销金额: {}, 个账支付: {}",
                totalAmount, selfPayAmount, deductibleAmount, withinScopeAmount, reimbursementAmount, personalAccountPay);

        return settlement;
    }

    private BigDecimal calculateSelfPayAmount(SettlementRequestDTO request) {
        BigDecimal selfPayAmount = BigDecimal.ZERO;
        if (request.getTreatmentItems() != null && !request.getTreatmentItems().isEmpty()) {
            for (TreatmentItemDTO item : request.getTreatmentItems()) {
                if ("N".equals(item.getWithinScope())) {
                    BigDecimal itemAmount = BigDecimalUtils.defaultIfNull(item.getAmount());
                    selfPayAmount = selfPayAmount.add(itemAmount);
                }
            }
        } else {
            BigDecimal totalAmount = BigDecimalUtils.defaultIfNull(request.getTotalAmount());
            selfPayAmount = totalAmount.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        }
        return selfPayAmount;
    }

    private PersonalAccount createDefaultAccount(InsuredPerson insuredPerson) {
        PersonalAccount account = new PersonalAccount();
        account.setPersonalAccountNo(BusinessNoGenerator.generatePersonalAccountNo());
        account.setIdCard(insuredPerson.getIdCard());
        account.setBalance(BigDecimal.ZERO);
        account.setFrozenAmount(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setTotalIncome(BigDecimal.ZERO);
        account.setTotalExpense(BigDecimal.ZERO);
        account.setAccountStatus("NORMAL");
        personalAccountMapper.insert(account);
        log.info("为参保人{}创建默认个人账户，账号: {}", insuredPerson.getName(), account.getPersonalAccountNo());
        return account;
    }

    private void createAccountFlow(PersonalAccount account, BigDecimal amount, FlowTypeEnum flowType,
                                   String businessType, String businessNo, String relatedAccount) {
        AccountFlowRecord flow = new AccountFlowRecord();
        flow.setFlowNo(BusinessNoGenerator.generateFlowNo());
        flow.setPersonalAccountNo(account.getPersonalAccountNo());
        flow.setIdCard(account.getIdCard());
        flow.setFlowType(flowType.getCode());
        flow.setAmount(BigDecimalUtils.defaultIfNull(amount));
        BigDecimal accountBalance = BigDecimalUtils.defaultIfNull(account.getBalance());
        flow.setBalanceBefore(BigDecimalUtils.safeAdd(accountBalance, amount));
        flow.setBalanceAfter(accountBalance);
        flow.setBusinessType(businessType);
        flow.setBusinessNo(businessNo);
        flow.setRelatedAccount(relatedAccount);
        flow.setOccurTime(LocalDateTime.now());
        flow.setOperator("system");
        flow.setRemark("异地结算个账支付");

        flowRecordMapper.insert(flow);
    }

    private SettlementResponseDTO buildResponse(SettlementRequestDTO request, SettlementRecord settlement,
                                                MedicalPolicy policy, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        SettlementResponseDTO response = new SettlementResponseDTO();
        response.setSettlementNo(settlement.getSettlementNo());
        response.setRequestNo(request.getRequestNo());
        response.setRecordNo(settlement.getRecordNo());
        response.setIdCard(request.getIdCard());
        response.setName(request.getName());
        response.setTotalAmount(settlement.getTotalAmount());
        response.setSelfPayAmount(settlement.getSelfPayAmount());
        response.setDeductibleAmount(settlement.getDeductibleAmount());
        response.setWithinScopeAmount(settlement.getWithinScopeAmount());
        response.setReimbursementAmount(settlement.getReimbursementAmount());
        response.setPersonalAccountPay(settlement.getPersonalAccountPay());
        response.setUnifiedFundPay(settlement.getUnifiedFundPay());
        response.setOtherFundPay(settlement.getOtherFundPay());
        response.setCashPay(settlement.getCashPay());
        response.setAccountBalanceBefore(balanceBefore);
        response.setAccountBalanceAfter(balanceAfter);
        response.setPolicyCode(policy.getPolicyCode());
        response.setSettlementStatus(settlement.getSettlementStatus());
        response.setSettlementTime(settlement.getSettlementTime());
        response.setRemark(settlement.getRemark());
        return response;
    }
}
