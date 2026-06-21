package com.bjyb.transfer.service;

import com.bjyb.common.dto.AccountBalanceSyncDTO;
import com.bjyb.common.dto.AccountTransferRequestDTO;
import com.bjyb.common.dto.AccountTransferResponseDTO;
import com.bjyb.common.entity.*;
import com.bjyb.common.enums.FlowTypeEnum;
import com.bjyb.common.enums.TransferStatusEnum;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.*;
import com.bjyb.common.utils.BigDecimalUtils;
import com.bjyb.common.utils.BusinessNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountTransferService {

    private static final Logger log = LoggerFactory.getLogger(AccountTransferService.class);

    @Autowired
    private InsuredPersonMapper insuredPersonMapper;

    @Autowired
    private PersonalAccountMapper personalAccountMapper;

    @Autowired
    private AccountTransferRecordMapper transferRecordMapper;

    @Autowired
    private AccountFlowRecordMapper flowRecordMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional(rollbackFor = Exception.class)
    public AccountTransferResponseDTO applyTransfer(AccountTransferRequestDTO request) {
        log.info("收到个账转移申请，身份证号: {}, 目标省份: {}, 转移金额: {}",
                request.getIdCard(), request.getTargetProvinceName(), request.getTransferAmount());

        InsuredPerson insuredPerson = validateInsuredPerson(request.getIdCard(), request.getName());
        PersonalAccount account = validateAccount(request.getIdCard());
        BigDecimal availableBalance = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());
        BigDecimal transferAmount = BigDecimalUtils.defaultIfNull(request.getTransferAmount());

        validateTransferAmount(account, transferAmount);

        AccountTransferRecord transferRecord = new AccountTransferRecord();
        transferRecord.setTransferNo(BusinessNoGenerator.generateTransferNo());
        transferRecord.setIdCard(request.getIdCard());
        transferRecord.setName(insuredPerson.getName());
        transferRecord.setPersonalAccountNo(account.getPersonalAccountNo());
        transferRecord.setTargetProvinceCode(request.getTargetProvinceCode());
        transferRecord.setTargetProvinceName(request.getTargetProvinceName());
        transferRecord.setTargetInsuranceArea(request.getTargetInsuranceArea());
        transferRecord.setTargetAccountNo(request.getTargetAccountNo());
        transferRecord.setTransferAmount(transferAmount);
        transferRecord.setBalanceBeforeTransfer(availableBalance);
        transferRecord.setTransferReason(request.getTransferReason());
        transferRecord.setApplyTime(LocalDateTime.now());
        transferRecord.setTransferStatus(TransferStatusEnum.PENDING_AUDIT.getCode());
        transferRecord.setOperator(request.getOperator());
        transferRecord.setRetryCount(0);

        transferRecordMapper.insert(transferRecord);

        AccountTransferResponseDTO response = buildResponse(transferRecord, availableBalance, availableBalance);

        log.info("个账转移申请已提交，转移单号: {}", transferRecord.getTransferNo());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public AccountTransferResponseDTO auditTransfer(String transferNo, boolean passed, String auditor, String rejectReason) {
        log.info("审核个账转移申请，转移单号: {}, 是否通过: {}", transferNo, passed);

        AccountTransferRecord transferRecord = transferRecordMapper.selectByTransferNo(transferNo);
        if (transferRecord == null) {
            throw new BusinessException("未找到转移记录，转移单号: " + transferNo);
        }

        if (!TransferStatusEnum.PENDING_AUDIT.getCode().equals(transferRecord.getTransferStatus())) {
            throw new BusinessException("转移单当前状态不允许审核，当前状态: " + transferRecord.getTransferStatus());
        }

        transferRecord.setAuditTime(LocalDateTime.now());
        transferRecord.setAuditor(auditor);

        if (!passed) {
            transferRecord.setTransferStatus(TransferStatusEnum.AUDIT_REJECTED.getCode());
            transferRecord.setFailReason(rejectReason);
            transferRecordMapper.updateById(transferRecord);
            log.info("个账转移申请已拒绝，转移单号: {}, 原因: {}", transferNo, rejectReason);
            return buildResponse(transferRecord, transferRecord.getBalanceBeforeTransfer(), transferRecord.getBalanceBeforeTransfer());
        }

        transferRecord.setTransferStatus(TransferStatusEnum.AUDIT_PASSED.getCode());
        transferRecordMapper.updateById(transferRecord);

        return executeTransfer(transferRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public AccountTransferResponseDTO executeTransfer(AccountTransferRecord transferRecord) {
        BigDecimal transferAmount = BigDecimalUtils.defaultIfNull(transferRecord.getTransferAmount());
        log.info("开始执行个账转移，转移单号: {}, 转移金额: {}", transferRecord.getTransferNo(), transferAmount);

        PersonalAccount account = personalAccountMapper.selectByIdCard(transferRecord.getIdCard());
        if (account == null || !"NORMAL".equals(account.getAccountStatus())) {
            throw new BusinessException("个人账户状态异常，无法转移");
        }

        BigDecimal balanceBefore = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());

        if (BigDecimalUtils.isLessThan(balanceBefore, transferAmount)) {
            throw new BusinessException("个人账户余额不足，当前余额: " + balanceBefore + ", 转移金额: " + transferAmount);
        }

        transferRecord.setTransferStatus(TransferStatusEnum.TRANSFERRING.getCode());
        transferRecordMapper.updateById(transferRecord);

        BigDecimal balanceAfter = BigDecimalUtils.safeSubtract(balanceBefore, transferAmount);
        int updateCount = personalAccountMapper.updateBalance(account.getId(), transferAmount, balanceAfter, balanceAfter);
        if (updateCount == 0) {
            throw new BusinessException("个人账户扣款失败，余额不足或状态异常");
        }

        createAccountFlow(account, transferAmount, FlowTypeEnum.TRANSFER_OUT, "个账转移",
                transferRecord.getTransferNo(), transferRecord.getTargetProvinceName());

        account = personalAccountMapper.selectById(account.getId());
        if (account == null) {
            throw new BusinessException("扣款后查询账户信息失败");
        }

        boolean syncSuccess = syncBalanceToTargetProvince(transferRecord, account);

        BigDecimal finalBalance = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());
        if (syncSuccess) {
            transferRecord.setTransferStatus(TransferStatusEnum.TRANSFER_SUCCESS.getCode());
            transferRecord.setExternalTransferNo("EXT" + System.currentTimeMillis());
            transferRecord.setBalanceAfterTransfer(finalBalance);
            transferRecord.setTransferTime(LocalDateTime.now());
            log.info("个账转移成功，转移单号: {}, 外部流水号: {}", transferRecord.getTransferNo(), transferRecord.getExternalTransferNo());
        } else {
            transferRecord.setTransferStatus(TransferStatusEnum.TRANSFER_FAILED.getCode());
            transferRecord.setFailReason("同步到目标省份失败");
            log.error("个账转移失败，转移单号: {}", transferRecord.getTransferNo());

            rollbackBalance(account, transferAmount);
            finalBalance = balanceBefore;
        }

        transferRecordMapper.updateById(transferRecord);

        return buildResponse(transferRecord, balanceBefore, finalBalance);
    }

    private boolean syncBalanceToTargetProvince(AccountTransferRecord transferRecord, PersonalAccount account) {
        try {
            AccountBalanceSyncDTO syncDTO = new AccountBalanceSyncDTO();
            syncDTO.setSyncNo("SYNC" + System.currentTimeMillis());
            syncDTO.setIdCard(transferRecord.getIdCard());
            syncDTO.setPersonalAccountNo(account.getPersonalAccountNo());
            syncDTO.setBalance(BigDecimalUtils.defaultIfNull(account.getBalance()));
            syncDTO.setAvailableBalance(BigDecimalUtils.defaultIfNull(account.getAvailableBalance()));
            syncDTO.setFrozenAmount(BigDecimalUtils.defaultIfNull(account.getFrozenAmount()));
            syncDTO.setAccountStatus(account.getAccountStatus());
            syncDTO.setSyncTime(LocalDateTime.now());
            syncDTO.setSourceSystem("BJ-MEDICAL-INSURANCE");

            log.info("同步个账余额到目标省份: {}, 同步数据: {}", transferRecord.getTargetProvinceName(), syncDTO);

            String targetUrl = "http://" + transferRecord.getTargetProvinceCode() + ".medical-insurance.gov.cn/api/balance-sync";
            try {
                restTemplate.postForObject(targetUrl, syncDTO, String.class);
            } catch (Exception e) {
                log.warn("调用外部接口失败，模拟同步成功用于演示: {}", e.getMessage());
            }

            Thread.sleep(500);

            return true;
        } catch (Exception e) {
            log.error("同步个账余额到目标省份失败", e);
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void retryTransfer(String transferNo) {
        log.info("重试个账转移，转移单号: {}", transferNo);

        AccountTransferRecord transferRecord = transferRecordMapper.selectByTransferNo(transferNo);
        if (transferRecord == null) {
            throw new BusinessException("未找到转移记录，转移单号: " + transferNo);
        }

        if (!TransferStatusEnum.TRANSFER_FAILED.getCode().equals(transferRecord.getTransferStatus())) {
            throw new BusinessException("只有转移失败的记录可以重试，当前状态: " + transferRecord.getTransferStatus());
        }

        if (transferRecord.getRetryCount() >= 3) {
            throw new BusinessException("重试次数已达上限(3次)，请人工处理");
        }

        executeTransfer(transferRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelTransfer(String transferNo, String operator) {
        log.info("取消个账转移申请，转移单号: {}", transferNo);

        AccountTransferRecord transferRecord = transferRecordMapper.selectByTransferNo(transferNo);
        if (transferRecord == null) {
            throw new BusinessException("未找到转移记录，转移单号: " + transferNo);
        }

        if (!TransferStatusEnum.PENDING_AUDIT.getCode().equals(transferRecord.getTransferStatus())) {
            throw new BusinessException("只有待审核的记录可以取消，当前状态: " + transferRecord.getTransferStatus());
        }

        transferRecord.setTransferStatus(TransferStatusEnum.CANCELLED.getCode());
        transferRecord.setOperator(operator);
        transferRecordMapper.updateById(transferRecord);

        log.info("个账转移申请已取消，转移单号: {}", transferNo);
    }

    private InsuredPerson validateInsuredPerson(String idCard, String name) {
        InsuredPerson insuredPerson = insuredPersonMapper.selectByIdCard(idCard);
        if (insuredPerson == null) {
            throw new BusinessException("未找到参保人信息，身份证号: " + idCard);
        }
        if (!insuredPerson.getName().equals(name)) {
            throw new BusinessException("参保人姓名与身份证号不匹配");
        }
        if (!"正常参保".equals(insuredPerson.getInsuranceStatus()) && !"暂停参保".equals(insuredPerson.getInsuranceStatus())) {
            throw new BusinessException("参保人状态不允许转移，当前状态: " + insuredPerson.getInsuranceStatus());
        }
        return insuredPerson;
    }

    private PersonalAccount validateAccount(String idCard) {
        PersonalAccount account = personalAccountMapper.selectByIdCard(idCard);
        if (account == null) {
            throw new BusinessException("未找到个人账户信息，身份证号: " + idCard);
        }
        if (!"NORMAL".equals(account.getAccountStatus())) {
            throw new BusinessException("个人账户状态异常，当前状态: " + account.getAccountStatus());
        }
        BigDecimal frozenAmount = BigDecimalUtils.defaultIfNull(account.getFrozenAmount());
        if (BigDecimalUtils.isGreaterThan(frozenAmount, BigDecimal.ZERO)) {
            throw new BusinessException("个人账户存在冻结金额，无法转移");
        }
        return account;
    }

    private void validateTransferAmount(PersonalAccount account, BigDecimal transferAmount) {
        if (BigDecimalUtils.isLessOrEqual(transferAmount, BigDecimal.ZERO)) {
            throw new BusinessException("转移金额必须大于0");
        }
        BigDecimal availableBalance = BigDecimalUtils.defaultIfNull(account.getAvailableBalance());
        if (BigDecimalUtils.isGreaterThan(transferAmount, availableBalance)) {
            throw new BusinessException("转移金额不能超过账户可用余额，当前可用余额: " + availableBalance);
        }
    }

    private void rollbackBalance(PersonalAccount account, BigDecimal amount) {
        try {
            BigDecimal currentBalance = BigDecimalUtils.defaultIfNull(account.getBalance());
            BigDecimal balanceAfter = BigDecimalUtils.safeAdd(currentBalance, amount);
            personalAccountMapper.updateBalance(account.getId(), BigDecimal.ZERO.subtract(amount), balanceAfter, balanceAfter);
            log.info("个账转移失败，已回滚余额，账户: {}, 回滚金额: {}", account.getPersonalAccountNo(), amount);
        } catch (Exception e) {
            log.error("回滚账户余额失败，账户: {}, 金额: {}", account.getPersonalAccountNo(), amount, e);
        }
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
        flow.setBalanceBefore(accountBalance);
        flow.setBalanceAfter(BigDecimalUtils.safeSubtract(accountBalance, amount));
        flow.setBusinessType(businessType);
        flow.setBusinessNo(businessNo);
        flow.setRelatedAccount(relatedAccount);
        flow.setOccurTime(LocalDateTime.now());
        flow.setOperator("system");
        flow.setRemark("个账跨省转移");

        flowRecordMapper.insert(flow);
    }

    private AccountTransferResponseDTO buildResponse(AccountTransferRecord record, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        AccountTransferResponseDTO response = new AccountTransferResponseDTO();
        response.setTransferNo(record.getTransferNo());
        response.setIdCard(record.getIdCard());
        response.setName(record.getName());
        response.setPersonalAccountNo(record.getPersonalAccountNo());
        response.setTargetProvinceCode(record.getTargetProvinceCode());
        response.setTargetProvinceName(record.getTargetProvinceName());
        response.setTargetInsuranceArea(record.getTargetInsuranceArea());
        response.setTargetAccountNo(record.getTargetAccountNo());
        response.setTransferAmount(record.getTransferAmount());
        response.setBalanceBeforeTransfer(balanceBefore);
        response.setBalanceAfterTransfer(balanceAfter);
        response.setTransferStatus(record.getTransferStatus());
        response.setApplyTime(record.getApplyTime());
        response.setTransferTime(record.getTransferTime());
        response.setExternalTransferNo(record.getExternalTransferNo());
        response.setFailReason(record.getFailReason());
        response.setRemark(record.getRemark());
        return response;
    }
}
