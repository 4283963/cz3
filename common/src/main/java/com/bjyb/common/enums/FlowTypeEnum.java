package com.bjyb.common.enums;

public enum FlowTypeEnum {

    INCOME("1", "收入"),
    EXPENSE("2", "支出"),
    TRANSFER_OUT("3", "转出"),
    TRANSFER_IN("4", "转入"),
    FROZEN("5", "冻结"),
    UNFROZEN("6", "解冻"),
    ADJUST("7", "调整");

    private final String code;
    private final String desc;

    FlowTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static FlowTypeEnum getByCode(String code) {
        for (FlowTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
