package com.bjyb.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountBalanceSyncDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "同步请求号不能为空")
    private String syncNo;

    @NotBlank(message = "参保人身份证号不能为空")
    private String idCard;

    @NotBlank(message = "个账账号不能为空")
    private String personalAccountNo;

    @NotNull(message = "账户余额不能为空")
    private BigDecimal balance;

    @NotNull(message = "可用余额不能为空")
    private BigDecimal availableBalance;

    @NotNull(message = "冻结金额不能为空")
    private BigDecimal frozenAmount;

    @NotBlank(message = "账户状态不能为空")
    private String accountStatus;

    private LocalDateTime lastSettlementDate;

    private LocalDateTime syncTime;

    private String sourceSystem;

    public String getSyncNo() {
        return syncNo;
    }

    public void setSyncNo(String syncNo) {
        this.syncNo = syncNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPersonalAccountNo() {
        return personalAccountNo;
    }

    public void setPersonalAccountNo(String personalAccountNo) {
        this.personalAccountNo = personalAccountNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDateTime getLastSettlementDate() {
        return lastSettlementDate;
    }

    public void setLastSettlementDate(LocalDateTime lastSettlementDate) {
        this.lastSettlementDate = lastSettlementDate;
    }

    public LocalDateTime getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(LocalDateTime syncTime) {
        this.syncTime = syncTime;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
}
