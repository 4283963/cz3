package com.bjyb.transfer.service;

import com.bjyb.common.dto.AccountBalanceSyncDTO;
import com.bjyb.common.dto.Result;
import com.bjyb.common.entity.AccountFlowRecord;
import com.bjyb.common.entity.PersonalAccount;
import com.bjyb.common.enums.FlowTypeEnum;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.AccountFlowRecordMapper;
import com.bjyb.common.mapper.PersonalAccountMapper;
import com.bjyb.common.utils.BusinessNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BalanceSyncService {

    private static final Logger log = LoggerFactory.getLogger(BalanceSyncService.class);

    @Autowired
    private PersonalAccountMapper personalAccountMapper;

    @Autowired
    private AccountFlowRecordMapper flowRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> syncAccountBalance(AccountBalanceSyncDTO syncDTO) {
        log.info("收到个账余额同步请求，同步号: {}, 身份证号: {}, 账户: {}",
                syncDTO.getSyncNo(), syncDTO.getIdCard(), syncDTO.getPersonalAccountNo());

        PersonalAccount account = personalAccountMapper.selectByAccountNo(syncDTO.getPersonalAccountNo());
        if (account == null) {
            log.warn("未找到个人账户信息，将创建新账户，账户号: {}", syncDTO.getPersonalAccountNo());
            account = createNewAccount(syncDTO);
        }

        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = syncDTO.getBalance();
        BigDecimal balanceDiff = newBalance.subtract(oldBalance);

        if (balanceDiff.compareTo(BigDecimal.ZERO) != 0) {
            account.setBalance(newBalance);
            account.setAvailableBalance(syncDTO.getAvailableBalance());
            account.setFrozenAmount(syncDTO.getFrozenAmount());
            account.setAccountStatus(syncDTO.getAccountStatus());
            if (syncDTO.getLastSettlementDate() != null) {
                account.setLastSettlementDate(syncDTO.getLastSettlementDate().toLocalDate());
            }

            if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                account.setTotalIncome(account.getTotalIncome().add(balanceDiff));
            } else {
                account.setTotalExpense(account.getTotalExpense().add(balanceDiff.abs()));
            }

            personalAccountMapper.updateById(account);
            createSyncFlow(account, balanceDiff, syncDTO.getSyncNo(), syncDTO.getSourceSystem());

            log.info("个账余额同步完成，账户: {}, 余额变动: {} ({} -> {})",
                    account.getPersonalAccountNo(), balanceDiff, oldBalance, newBalance);
        } else {
            log.info("个账余额无变动，跳过更新，账户: {}", account.getPersonalAccountNo());
        }

        return Result.success("余额同步成功", syncDTO.getSyncNo());
    }

    private PersonalAccount createNewAccount(AccountBalanceSyncDTO syncDTO) {
        PersonalAccount account = new PersonalAccount();
        account.setPersonalAccountNo(syncDTO.getPersonalAccountNo());
        account.setIdCard(syncDTO.getIdCard());
        account.setBalance(syncDTO.getBalance());
        account.setAvailableBalance(syncDTO.getAvailableBalance());
        account.setFrozenAmount(syncDTO.getFrozenAmount());
        account.setTotalIncome(syncDTO.getBalance());
        account.setTotalExpense(BigDecimal.ZERO);
        account.setAccountStatus(syncDTO.getAccountStatus());
        if (syncDTO.getLastSettlementDate() != null) {
            account.setLastSettlementDate(syncDTO.getLastSettlementDate().toLocalDate());
        }

        personalAccountMapper.insert(account);
        log.info("已创建新个人账户，账户号: {}", account.getPersonalAccountNo());
        return account;
    }

    private void createSyncFlow(PersonalAccount account, BigDecimal amount, String syncNo, String sourceSystem) {
        AccountFlowRecord flow = new AccountFlowRecord();
        flow.setFlowNo(BusinessNoGenerator.generateFlowNo());
        flow.setPersonalAccountNo(account.getPersonalAccountNo());
        flow.setIdCard(account.getIdCard());

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            flow.setFlowType(FlowTypeEnum.TRANSFER_IN.getCode());
            flow.setAmount(amount);
            flow.setBalanceBefore(account.getBalance().subtract(amount));
            flow.setBalanceAfter(account.getBalance());
            flow.setBusinessType("跨省转入");
        } else {
            flow.setFlowType(FlowTypeEnum.TRANSFER_OUT.getCode());
            flow.setAmount(amount.abs());
            flow.setBalanceBefore(account.getBalance().add(amount.abs()));
            flow.setBalanceAfter(account.getBalance());
            flow.setBusinessType("跨省转出");
        }

        flow.setBusinessNo(syncNo);
        flow.setRelatedAccount(sourceSystem);
        flow.setOccurTime(LocalDateTime.now());
        flow.setOperator("system");
        flow.setRemark("跨省个账余额同步");

        flowRecordMapper.insert(flow);
    }

    public PersonalAccount getAccountBalance(String idCard) {
        log.info("查询个人账户余额，身份证号: {}", idCard);
        PersonalAccount account = personalAccountMapper.selectByIdCard(idCard);
        if (account == null) {
            throw new BusinessException("未找到个人账户信息，身份证号: " + idCard);
        }
        return account;
    }

    public PersonalAccount getAccountByNo(String accountNo) {
        log.info("查询个人账户信息，账户号: {}", accountNo);
        PersonalAccount account = personalAccountMapper.selectByAccountNo(accountNo);
        if (account == null) {
            throw new BusinessException("未找到个人账户信息，账户号: " + accountNo);
        }
        return account;
    }
}
