package com.qoobot.openaccounting.common.enums;

/**
 * 是否枚举
 *
 * @author openaccounting
 */
public enum YesNoEnum {

    /**
     * 是
     */
    YES(1, "是"),

    /**
     * 否
     */
    NO(0, "否");

    private final Integer code;
    private final String desc;

    YesNoEnum(Integer code, String desc) {
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
    public static YesNoEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (YesNoEnum e : YesNoEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
