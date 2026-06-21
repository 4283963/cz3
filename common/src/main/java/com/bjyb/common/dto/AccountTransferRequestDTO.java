package com.bjyb.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTransferRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestNo;

    @NotBlank(message = "参保人身份证号不能为空")
    private String idCard;

    @NotBlank(message = "参保人姓名不能为空")
    private String name;

    @NotBlank(message = "目标省份编码不能为空")
    private String targetProvinceCode;

    @NotBlank(message = "目标省份名称不能为空")
    private String targetProvinceName;

    private String targetInsuranceArea;

    private String targetAccountNo;

    @NotNull(message = "转移金额不能为空")
    private BigDecimal transferAmount;

    private String transferReason;

    private String operator;

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

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
