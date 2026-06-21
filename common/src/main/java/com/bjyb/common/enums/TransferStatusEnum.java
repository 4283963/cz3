package com.bjyb.common.enums;

public enum TransferStatusEnum {

    PENDING_AUDIT("0", "待审核"),
    AUDIT_PASSED("1", "审核通过"),
    AUDIT_REJECTED("2", "审核拒绝"),
    TRANSFERRING("3", "转移中"),
    TRANSFER_SUCCESS("4", "转移成功"),
    TRANSFER_FAILED("5", "转移失败"),
    CANCELLED("6", "已取消");

    private final String code;
    private final String desc;

    TransferStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TransferStatusEnum getByCode(String code) {
        for (TransferStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
