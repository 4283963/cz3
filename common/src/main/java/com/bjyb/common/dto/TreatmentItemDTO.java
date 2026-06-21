package com.bjyb.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TreatmentItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemCode;

    private String itemName;

    private String itemType;

    private String specification;

    private String unit;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal amount;

    private String withinScope;

    private String reimbursementCategory;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getWithinScope() {
        return withinScope;
    }

    public void setWithinScope(String withinScope) {
        this.withinScope = withinScope;
    }

    public String getReimbursementCategory() {
        return reimbursementCategory;
    }

    public void setReimbursementCategory(String reimbursementCategory) {
        this.reimbursementCategory = reimbursementCategory;
    }
}
