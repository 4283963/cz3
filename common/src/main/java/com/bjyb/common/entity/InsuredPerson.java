package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;

@TableName("insured_person")
public class InsuredPerson extends BaseEntity {

    private String idCard;

    private String name;

    private String gender;

    private LocalDate birthDate;

    private String insuranceType;

    private String insuranceStatus;

    private BigDecimal accountBalance;

    private BigDecimal cumulativePayment;

    private String workUnit;

    private String contactPhone;

    private String address;

    private String personalAccountNo;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(String insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getCumulativePayment() {
        return cumulativePayment;
    }

    public void setCumulativePayment(BigDecimal cumulativePayment) {
        this.cumulativePayment = cumulativePayment;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalAccountNo() {
        return personalAccountNo;
    }

    public void setPersonalAccountNo(String personalAccountNo) {
        this.personalAccountNo = personalAccountNo;
    }
}
