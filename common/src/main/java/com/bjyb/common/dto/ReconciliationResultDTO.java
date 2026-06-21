package com.bjyb.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReconciliationResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDate;

    private Integer totalBills;

    private Integer matchedBills;

    private Integer mismatchedBills;

    private Integer missingLocalBills;

    private Integer missingExternalBills;

    private BigDecimal totalLocalAmount;

    private BigDecimal totalExternalAmount;

    private BigDecimal diffAmount;

    private String batchStatus;

    private Integer exceptionCount;

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

    public Integer getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(Integer exceptionCount) {
        this.exceptionCount = exceptionCount;
    }
}
