package com.qoobot.openaccounting.common.enums;

import lombok.Getter;

/**
 * 会计期间状态枚举
 *
 * @author openaccounting
 */
@Getter
public enum AccountingPeriodStatus {

    OPEN("open", "已开启"),
    CLOSED("closed", "已结账"),
    LOCKED("locked", "已锁定");

    private final String code;
    private final String desc;

    AccountingPeriodStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AccountingPeriodStatus fromCode(String code) {
        for (AccountingPeriodStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
