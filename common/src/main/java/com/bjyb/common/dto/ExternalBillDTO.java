package com.bjyb.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExternalBillDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
