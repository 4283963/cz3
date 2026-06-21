package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;

@TableName("medical_policy")
public class MedicalPolicy extends BaseEntity {

    private String policyCode;

    private String policyName;

    private String insuranceType;

    private String treatmentType;

    private BigDecimal deductible;

    private BigDecimal reimbursementRatio;

    private BigDecimal maxReimbursementAmount;

    private String applicableScope;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private String policyStatus;

    private String description;

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public BigDecimal getDeductible() {
        return deductible;
    }

    public void setDeductible(BigDecimal deductible) {
        this.deductible = deductible;
    }

    public BigDecimal getReimbursementRatio() {
        return reimbursementRatio;
    }

    public void setReimbursementRatio(BigDecimal reimbursementRatio) {
        this.reimbursementRatio = reimbursementRatio;
    }

    public BigDecimal getMaxReimbursementAmount() {
        return maxReimbursementAmount;
    }

    public void setMaxReimbursementAmount(BigDecimal maxReimbursementAmount) {
        this.maxReimbursementAmount = maxReimbursementAmount;
    }

    public String getApplicableScope() {
        return applicableScope;
    }

    public void setApplicableScope(String applicableScope) {
        this.applicableScope = applicableScope;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
