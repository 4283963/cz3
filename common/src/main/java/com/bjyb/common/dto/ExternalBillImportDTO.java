package com.bjyb.common.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

public class ExternalBillImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "单据列表不能为空")
    @Valid
    private List<ExternalBillDTO> bills;

    @NotBlank(message = "来源系统不能为空")
    private String sourceSystem;

    private String operator;

    public List<ExternalBillDTO> getBills() {
        return bills;
    }

    public void setBills(List<ExternalBillDTO> bills) {
        this.bills = bills;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
