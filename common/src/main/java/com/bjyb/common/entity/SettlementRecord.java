package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("settlement_record")
public class SettlementRecord extends BaseEntity {

    private String settlementNo;

    private String recordNo;

    private String idCard;

    private String name;

    private String hospitalCode;

    private String hospitalName;

    private String provinceCode;

    private String provinceName;

    private String policyCode;

    private String medicalType;

    private BigDecimal totalAmount;

    private BigDecimal selfPayAmount;

    private BigDecimal deductibleAmount;

    private BigDecimal withinScopeAmount;

    private BigDecimal reimbursementAmount;

    private BigDecimal personalAccountPay;

    private BigDecimal unifiedFundPay;

    private BigDecimal otherFundPay;

    private BigDecimal cashPay;

    private String settlementType;

    private LocalDateTime settlementTime;

    private String settlementStatus;

    private String operator;

    private String remark;

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
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

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
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

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public LocalDateTime getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(LocalDateTime settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
