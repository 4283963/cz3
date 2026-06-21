package com.bjyb.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SettlementResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String settlementNo;

    private String requestNo;

    private String recordNo;

    private String idCard;

    private String name;

    private BigDecimal totalAmount;

    private BigDecimal selfPayAmount;

    private BigDecimal deductibleAmount;

    private BigDecimal withinScopeAmount;

    private BigDecimal reimbursementAmount;

    private BigDecimal personalAccountPay;

    private BigDecimal unifiedFundPay;

    private BigDecimal otherFundPay;

    private BigDecimal cashPay;

    private BigDecimal accountBalanceBefore;

    private BigDecimal accountBalanceAfter;

    private String policyCode;

    private String settlementStatus;

    private LocalDateTime settlementTime;

    private String remark;

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSelfPayAmount() {
        return selfPayAmount;
    }

    public void setSelfPayAmount(BigDecimal selfPayAmount) {
        this.selfPayAmount = selfPayAmount;
    }

    public BigDecimal getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(BigDecimal deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public BigDecimal getWithinScopeAmount() {
        return withinScopeAmount;
    }

    public void setWithinScopeAmount(BigDecimal withinScopeAmount) {
        this.withinScopeAmount = withinScopeAmount;
    }

    public BigDecimal getReimbursementAmount() {
        return reimbursementAmount;
    }

    public void setReimbursementAmount(BigDecimal reimbursementAmount) {
        this.reimbursementAmount = reimbursementAmount;
    }

    public BigDecimal getPersonalAccountPay() {
        return personalAccountPay;
    }

    public void setPersonalAccountPay(BigDecimal personalAccountPay) {
        this.personalAccountPay = personalAccountPay;
    }

    public BigDecimal getUnifiedFundPay() {
        return unifiedFundPay;
    }

    public void setUnifiedFundPay(BigDecimal unifiedFundPay) {
        this.unifiedFundPay = unifiedFundPay;
    }

    public BigDecimal getOtherFundPay() {
        return otherFundPay;
    }

    public void setOtherFundPay(BigDecimal otherFundPay) {
        this.otherFundPay = otherFundPay;
    }

    public BigDecimal getCashPay() {
        return cashPay;
    }

    public void setCashPay(BigDecimal cashPay) {
        this.cashPay = cashPay;
    }

    public BigDecimal getAccountBalanceBefore() {
        return accountBalanceBefore;
    }

    public void setAccountBalanceBefore(BigDecimal accountBalanceBefore) {
        this.accountBalanceBefore = accountBalanceBefore;
    }

    public BigDecimal getAccountBalanceAfter() {
        return accountBalanceAfter;
    }

    public void setAccountBalanceAfter(BigDecimal accountBalanceAfter) {
        this.accountBalanceAfter = accountBalanceAfter;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public LocalDateTime getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(LocalDateTime settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
