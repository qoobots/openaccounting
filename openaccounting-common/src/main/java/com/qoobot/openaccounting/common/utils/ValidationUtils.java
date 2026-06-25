package com.qoobot.openaccounting.common.utils;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * 校验工具类
 *
 * @author openaccounting
 */
public class ValidationUtils {

    /**
     * 校验字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 校验字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 校验集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 校验集合是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 校验对象是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj instanceof Collection) {
            return isEmpty((Collection<?>) obj);
        }
        return false;
    }

    /**
     * 校验对象是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 校验金额是否有效
     */
    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 校验金额是否大于零
     */
    public static boolean isPositiveAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 校验借贷平衡
     */
    public static boolean isBalanceValid(BigDecimal debitAmount, BigDecimal creditAmount) {
        if (debitAmount == null || creditAmount == null) {
            return false;
        }
        return debitAmount.compareTo(creditAmount) == 0;
    }

    /**
     * 校验会计期间
     */
    public static boolean isValidPeriod(Integer year, Integer period) {
        if (year == null || period == null) {
            return false;
        }
        return year > 0 && year < 9999 && period >= 1 && period <= 12;
    }

    /**
     * 校验邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    /**
     * 校验手机号格式
     */
    public static boolean isValidMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        }
        String regex = "^1[3-9]\\d{9}$";
        return mobile.matches(regex);
    }

    /**
     * 校验身份证号格式
     */
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        String regex = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$";
        return idCard.matches(regex);
    }
}
