package com.bjyb.common.enums;

public enum ExceptionTypeEnum {

    AMOUNT_DIFF("AMOUNT_DIFF", "金额不符"),
    MISSING_LOCAL("MISSING_LOCAL", "本地缺失单据"),
    MISSING_EXTERNAL("MISSING_EXTERNAL", "外省缺失单据"),
    DUPLICATE("DUPLICATE", "重复单据");

    private final String code;
    private final String desc;

    ExceptionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ExceptionTypeEnum getByCode(String code) {
        for (ExceptionTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
