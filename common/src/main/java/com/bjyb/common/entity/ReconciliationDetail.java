package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("reconciliation_detail")
public class ReconciliationDetail extends BaseEntity {

    private String batchNo;

    private String externalBillNo;

    private String localSettlementNo;

    private String hospitalCode;

    private String idCard;

    private String name;

    private String medicalType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate treatmentDate;

    private BigDecimal localTotalAmount;

    private BigDecimal localReimbursementAmount;

    private BigDecimal localPersonalPay;

    private BigDecimal externalTotalAmount;

    private BigDecimal externalReimbursementAmount;

    private BigDecimal externalPersonalPay;

    private BigDecimal totalDiff;

    private BigDecimal reimbursementDiff;

    private BigDecimal personalPayDiff;

    private String matchStatus;

    private String reconciliationResult;

    private Integer isException;

    private Integer exceptionHandled;

    private String handler;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    private String handleRemark;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExternalBillNo() {
        return externalBillNo;
    }

    public void setExternalBillNo(String externalBillNo) {
        this.externalBillNo = externalBillNo;
    }

    public String getLocalSettlementNo() {
        return localSettlementNo;
    }

    public void setLocalSettlementNo(String localSettlementNo) {
        this.localSettlementNo = localSettlementNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
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

    public String getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(String medicalType) {
        this.medicalType = medicalType;
    }

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public BigDecimal getLocalTotalAmount() {
        return localTotalAmount;
    }

    public void setLocalTotalAmount(BigDecimal localTotalAmount) {
        this.localTotalAmount = localTotalAmount;
    }

    public BigDecimal getLocalReimbursementAmount() {
        return localReimbursementAmount;
    }

    public void setLocalReimbursementAmount(BigDecimal localReimbursementAmount) {
        this.localReimbursementAmount = localReimbursementAmount;
    }

    public BigDecimal getLocalPersonalPay() {
        return localPersonalPay;
    }

    public void setLocalPersonalPay(BigDecimal localPersonalPay) {
        this.localPersonalPay = localPersonalPay;
    }

    public BigDecimal getExternalTotalAmount() {
        return externalTotalAmount;
    }

    public void setExternalTotalAmount(BigDecimal externalTotalAmount) {
        this.externalTotalAmount = externalTotalAmount;
    }

    public BigDecimal getExternalReimbursementAmount() {
        return externalReimbursementAmount;
    }

    public void setExternalReimbursementAmount(BigDecimal externalReimbursementAmount) {
        this.externalReimbursementAmount = externalReimbursementAmount;
    }

    public BigDecimal getExternalPersonalPay() {
        return externalPersonalPay;
    }

    public void setExternalPersonalPay(BigDecimal externalPersonalPay) {
        this.externalPersonalPay = externalPersonalPay;
    }

    public BigDecimal getTotalDiff() {
        return totalDiff;
    }

    public void setTotalDiff(BigDecimal totalDiff) {
        this.totalDiff = totalDiff;
    }

    public BigDecimal getReimbursementDiff() {
        return reimbursementDiff;
    }

    public void setReimbursementDiff(BigDecimal reimbursementDiff) {
        this.reimbursementDiff = reimbursementDiff;
    }

    public BigDecimal getPersonalPayDiff() {
        return personalPayDiff;
    }

    public void setPersonalPayDiff(BigDecimal personalPayDiff) {
        this.personalPayDiff = personalPayDiff;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getReconciliationResult() {
        return reconciliationResult;
    }

    public void setReconciliationResult(String reconciliationResult) {
        this.reconciliationResult = reconciliationResult;
    }

    public Integer getIsException() {
        return isException;
    }

    public void setIsException(Integer isException) {
        this.isException = isException;
    }

    public Integer getExceptionHandled() {
        return exceptionHandled;
    }

    public void setExceptionHandled(Integer exceptionHandled) {
        this.exceptionHandled = exceptionHandled;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }
}
