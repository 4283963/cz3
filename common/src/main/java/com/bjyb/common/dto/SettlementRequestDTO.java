package com.bjyb.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SettlementRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号不能为空")
    private String requestNo;

    @NotBlank(message = "医院编码不能为空")
    private String hospitalCode;

    private String hospitalName;

    @NotBlank(message = "参保人身份证号不能为空")
    private String idCard;

    @NotBlank(message = "参保人姓名不能为空")
    private String name;

    @NotBlank(message = "医疗类别不能为空")
    private String medicalType;

    @NotNull(message = "总费用不能为空")
    private BigDecimal totalAmount;

    private LocalDate admissionDate;

    private LocalDate dischargeDate;

    private String diagnosis;

    private String invoiceNo;

    private List<TreatmentItemDTO> treatmentItems;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
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

    public List<TreatmentItemDTO> getTreatmentItems() {
        return treatmentItems;
    }

    public void setTreatmentItems(List<TreatmentItemDTO> treatmentItems) {
        this.treatmentItems = treatmentItems;
    }
}
