package com.bjyb.common.enums;

public enum MatchStatusEnum {

    MATCHED("MATCHED", "对账一致"),
    MISMATCHED("MISMATCHED", "对账不一致"),
    MISSING_LOCAL("MISSING_LOCAL", "本地缺失"),
    MISSING_EXTERNAL("MISSING_EXTERNAL", "外省缺失");

    private final String code;
    private final String desc;

    MatchStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MatchStatusEnum getByCode(String code) {
        for (MatchStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
