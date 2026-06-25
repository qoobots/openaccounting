package com.qoobot.openaccounting.common.enums;

import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author openaccounting
 */
@Getter
public enum CommonStatus {

    ENABLED(1, "启用"),
    DISABLED(0, "停用"),
    DELETED(-1, "已删除");

    private final Integer code;
    private final String desc;

    CommonStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommonStatus fromCode(Integer code) {
        for (CommonStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
