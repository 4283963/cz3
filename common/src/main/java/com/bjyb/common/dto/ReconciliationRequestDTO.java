package com.bjyb.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class ReconciliationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "对账日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reconciliationDate;

    @NotBlank(message = "省份编码不能为空")
    private String provinceCode;

    private String provinceName;

    private String operator;

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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
