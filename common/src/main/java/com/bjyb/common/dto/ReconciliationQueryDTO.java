package com.bjyb.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class ReconciliationQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDateEnd;

    private String provinceCode;

    private String billStatus;

    private String matchStatus;

    private String exceptionStatus;

    private String batchStatus;

    private String idCard;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getReconciliationDateStart() {
        return reconciliationDateStart;
    }

    public void setReconciliationDateStart(LocalDate reconciliationDateStart) {
        this.reconciliationDateStart = reconciliationDateStart;
    }

    public LocalDate getReconciliationDateEnd() {
        return reconciliationDateEnd;
    }

    public void setReconciliationDateEnd(LocalDate reconciliationDateEnd) {
        this.reconciliationDateEnd = reconciliationDateEnd;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(String exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
