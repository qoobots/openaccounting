package com.qoobot.openaccounting.common.enums;

/**
 * 删除标记枚举
 *
 * @author openaccounting
 */
public enum DeletedEnum {

    /**
     * 未删除
     */
    NORMAL(0, "未删除"),

    /**
     * 已删除
     */
    DELETED(1, "已删除");

    private final Integer code;
    private final String desc;

    DeletedEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     */
    public static DeletedEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DeletedEnum e : DeletedEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
