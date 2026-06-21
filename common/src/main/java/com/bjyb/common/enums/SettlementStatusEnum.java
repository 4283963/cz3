package com.bjyb.common.enums;

public enum SettlementStatusEnum {

    PENDING("0", "待结算"),
    SUCCESS("1", "结算成功"),
    FAILED("2", "结算失败"),
    CANCELLED("3", "已撤销"),
    REVERSED("4", "已冲正");

    private final String code;
    private final String desc;

    SettlementStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SettlementStatusEnum getByCode(String code) {
        for (SettlementStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
