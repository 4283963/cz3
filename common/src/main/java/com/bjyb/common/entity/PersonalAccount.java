package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;

@TableName("personal_account")
public class PersonalAccount extends BaseEntity {

    private String personalAccountNo;

    private String idCard;

    private BigDecimal balance;

    private BigDecimal frozenAmount;

    private BigDecimal availableBalance;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private LocalDate lastSettlementDate;

    private String accountStatus;

    public String getPersonalAccountNo() {
        return personalAccountNo;
    }

    public void setPersonalAccountNo(String personalAccountNo) {
        this.personalAccountNo = personalAccountNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public LocalDate getLastSettlementDate() {
        return lastSettlementDate;
    }

    public void setLastSettlementDate(LocalDate lastSettlementDate) {
        this.lastSettlementDate = lastSettlementDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
