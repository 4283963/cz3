package com.bjyb.common.enums;

public enum ExceptionStatusEnum {

    PENDING("PENDING", "待处理"),
    PROCESSING("PROCESSING", "处理中"),
    RESOLVED("RESOLVED", "已处理"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String desc;

    ExceptionStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ExceptionStatusEnum getByCode(String code) {
        for (ExceptionStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
