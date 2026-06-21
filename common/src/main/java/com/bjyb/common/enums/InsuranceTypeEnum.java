package com.bjyb.common.enums;

public enum InsuranceTypeEnum {

    EMPLOYEE("1", "职工医保"),
    RESIDENT("2", "居民医保"),
    RETIREE("3", "退休人员医保");

    private final String code;
    private final String desc;

    InsuranceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InsuranceTypeEnum getByCode(String code) {
        for (InsuranceTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
