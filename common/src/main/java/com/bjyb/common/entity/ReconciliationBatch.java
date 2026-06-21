package com.bjyb.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("reconciliation_batch")
public class ReconciliationBatch extends BaseEntity {

    private String batchNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDate;

    private String provinceCode;

    private String provinceName;

    private Integer totalBills;

    private Integer matchedBills;

    private Integer mismatchedBills;

    private Integer missingLocalBills;

    private Integer missingExternalBills;

    private BigDecimal totalLocalAmount;

    private BigDecimal totalExternalAmount;

    private BigDecimal diffAmount;

    private String batchStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String operator;

    private String remark;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getReconciliationDate() {
        return reconciliationDate;
    }

    public void setReconciliationDate(LocalDate reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
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

    public Integer getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(Integer totalBills) {
        this.totalBills = totalBills;
    }

    public Integer getMatchedBills() {
        return matchedBills;
    }

    public void setMatchedBills(Integer matchedBills) {
        this.matchedBills = matchedBills;
    }

    public Integer getMismatchedBills() {
        return mismatchedBills;
    }

    public void setMismatchedBills(Integer mismatchedBills) {
        this.mismatchedBills = mismatchedBills;
    }

    public Integer getMissingLocalBills() {
        return missingLocalBills;
    }

    public void setMissingLocalBills(Integer missingLocalBills) {
        this.missingLocalBills = missingLocalBills;
    }

    public Integer getMissingExternalBills() {
        return missingExternalBills;
    }

    public void setMissingExternalBills(Integer missingExternalBills) {
        this.missingExternalBills = missingExternalBills;
    }

    public BigDecimal getTotalLocalAmount() {
        return totalLocalAmount;
    }

    public void setTotalLocalAmount(BigDecimal totalLocalAmount) {
        this.totalLocalAmount = totalLocalAmount;
    }

    public BigDecimal getTotalExternalAmount() {
        return totalExternalAmount;
    }

    public void setTotalExternalAmount(BigDecimal totalExternalAmount) {
        this.totalExternalAmount = totalExternalAmount;
    }

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
