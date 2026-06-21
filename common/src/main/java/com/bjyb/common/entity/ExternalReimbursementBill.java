package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("external_reimbursement_bill")
public class ExternalReimbursementBill extends BaseEntity {

    private String externalBillNo;

    private String hospitalCode;

    private String hospitalName;

    private String provinceCode;

    private String provinceName;

    private String idCard;

    private String name;

    private String medicalType;

    private BigDecimal totalAmount;

    private BigDecimal reimbursementAmount;

    private BigDecimal personalPayAmount;

    private BigDecimal accountPayAmount;

    private BigDecimal cashPayAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate treatmentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dischargeDate;

    private String diagnosis;

    private String invoiceNo;

    private String billStatus;

    private String sourceSystem;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime importTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reconciliationTime;

    private String reconciliationBatchNo;

    private String remark;

    public String getExternalBillNo() {
        return externalBillNo;
    }

    public void setExternalBillNo(String externalBillNo) {
        this.externalBillNo = externalBillNo;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getReimbursementAmount() {
        return reimbursementAmount;
    }

    public void setReimbursementAmount(BigDecimal reimbursementAmount) {
        this.reimbursementAmount = reimbursementAmount;
    }

    public BigDecimal getPersonalPayAmount() {
        return personalPayAmount;
    }

    public void setPersonalPayAmount(BigDecimal personalPayAmount) {
        this.personalPayAmount = personalPayAmount;
    }

    public BigDecimal getAccountPayAmount() {
        return accountPayAmount;
    }

    public void setAccountPayAmount(BigDecimal accountPayAmount) {
        this.accountPayAmount = accountPayAmount;
    }

    public BigDecimal getCashPayAmount() {
        return cashPayAmount;
    }

    public void setCashPayAmount(BigDecimal cashPayAmount) {
        this.cashPayAmount = cashPayAmount;
    }

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public LocalDateTime getImportTime() {
        return importTime;
    }

    public void setImportTime(LocalDateTime importTime) {
        this.importTime = importTime;
    }

    public LocalDateTime getReconciliationTime() {
        return reconciliationTime;
    }

    public void setReconciliationTime(LocalDateTime reconciliationTime) {
        this.reconciliationTime = reconciliationTime;
    }

    public String getReconciliationBatchNo() {
        return reconciliationBatchNo;
    }

    public void setReconciliationBatchNo(String reconciliationBatchNo) {
        this.reconciliationBatchNo = reconciliationBatchNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
