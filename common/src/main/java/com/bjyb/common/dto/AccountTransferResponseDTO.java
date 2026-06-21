package com.bjyb.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountTransferResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String transferNo;

    private String requestNo;

    private String idCard;

    private String name;

    private String personalAccountNo;

    private String targetProvinceCode;

    private String targetProvinceName;

    private String targetInsuranceArea;

    private String targetAccountNo;

    private BigDecimal transferAmount;

    private BigDecimal balanceBeforeTransfer;

    private BigDecimal balanceAfterTransfer;

    private String transferStatus;

    private LocalDateTime applyTime;

    private LocalDateTime transferTime;

    private String externalTransferNo;

    private String failReason;

    private String remark;

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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

    public String getPersonalAccountNo() {
        return personalAccountNo;
    }

    public void setPersonalAccountNo(String personalAccountNo) {
        this.personalAccountNo = personalAccountNo;
    }

    public String getTargetProvinceCode() {
        return targetProvinceCode;
    }

    public void setTargetProvinceCode(String targetProvinceCode) {
        this.targetProvinceCode = targetProvinceCode;
    }

    public String getTargetProvinceName() {
        return targetProvinceName;
    }

    public void setTargetProvinceName(String targetProvinceName) {
        this.targetProvinceName = targetProvinceName;
    }

    public String getTargetInsuranceArea() {
        return targetInsuranceArea;
    }

    public void setTargetInsuranceArea(String targetInsuranceArea) {
        this.targetInsuranceArea = targetInsuranceArea;
    }

    public String getTargetAccountNo() {
        return targetAccountNo;
    }

    public void setTargetAccountNo(String targetAccountNo) {
        this.targetAccountNo = targetAccountNo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getBalanceBeforeTransfer() {
        return balanceBeforeTransfer;
    }

    public void setBalanceBeforeTransfer(BigDecimal balanceBeforeTransfer) {
        this.balanceBeforeTransfer = balanceBeforeTransfer;
    }

    public BigDecimal getBalanceAfterTransfer() {
        return balanceAfterTransfer;
    }

    public void setBalanceAfterTransfer(BigDecimal balanceAfterTransfer) {
        this.balanceAfterTransfer = balanceAfterTransfer;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public LocalDateTime getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(LocalDateTime transferTime) {
        this.transferTime = transferTime;
    }

    public String getExternalTransferNo() {
        return externalTransferNo;
    }

    public void setExternalTransferNo(String externalTransferNo) {
        this.externalTransferNo = externalTransferNo;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
