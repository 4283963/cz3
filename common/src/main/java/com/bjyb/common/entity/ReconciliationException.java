package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("reconciliation_exception")
public class ReconciliationException extends BaseEntity {

    private String exceptionNo;

    private String batchNo;

    private Long reconciliationDetailId;

    private String externalBillNo;

    private String localSettlementNo;

    private String hospitalCode;

    private String hospitalName;

    private String provinceCode;

    private String provinceName;

    private String idCard;

    private String name;

    private String medicalType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate treatmentDate;

    private String exceptionType;

    private BigDecimal localTotalAmount;

    private BigDecimal localReimbursementAmount;

    private BigDecimal externalTotalAmount;

    private BigDecimal externalReimbursementAmount;

    private BigDecimal totalDiff;

    private BigDecimal reimbursementDiff;

    private String exceptionDesc;

    private String exceptionStatus;

    private String handler;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    private String handleMethod;

    private String handleRemark;

    private String followUpPerson;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextFollowUpDate;

    public String getExceptionNo() {
        return exceptionNo;
    }

    public void setExceptionNo(String exceptionNo) {
        this.exceptionNo = exceptionNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getReconciliationDetailId() {
        return reconciliationDetailId;
    }

    public void setReconciliationDetailId(Long reconciliationDetailId) {
        this.reconciliationDetailId = reconciliationDetailId;
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

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
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

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(String exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
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

    public String getHandleMethod() {
        return handleMethod;
    }

    public void setHandleMethod(String handleMethod) {
        this.handleMethod = handleMethod;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public String getFollowUpPerson() {
        return followUpPerson;
    }

    public void setFollowUpPerson(String followUpPerson) {
        this.followUpPerson = followUpPerson;
    }

    public LocalDate getNextFollowUpDate() {
        return nextFollowUpDate;
    }

    public void setNextFollowUpDate(LocalDate nextFollowUpDate) {
        this.nextFollowUpDate = nextFollowUpDate;
    }
}
