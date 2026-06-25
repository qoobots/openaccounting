package com.qoobot.openaccounting.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * ValidationUtils单元测试
 *
 * @author openaccounting
 */
class ValidationUtilsTest {

    @Test
    void testIsEmpty() {
        assertTrue(ValidationUtils.isEmpty(null));
        assertTrue(ValidationUtils.isEmpty(""));
        assertTrue(ValidationUtils.isEmpty("   "));
        assertFalse(ValidationUtils.isEmpty("test"));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(ValidationUtils.isNotEmpty(null));
        assertFalse(ValidationUtils.isNotEmpty(""));
        assertTrue(ValidationUtils.isNotEmpty("test"));
    }

    @Test
    void testIsValidAmount() {
        assertTrue(ValidationUtils.isValidAmount(BigDecimal.ZERO));
        assertTrue(ValidationUtils.isValidAmount(new BigDecimal("100.00")));
        assertFalse(ValidationUtils.isValidAmount(null));
        assertFalse(ValidationUtils.isValidAmount(new BigDecimal("-1.00")));
    }

    @Test
    void testIsPositiveAmount() {
        assertTrue(ValidationUtils.isPositiveAmount(new BigDecimal("100.00")));
        assertFalse(ValidationUtils.isPositiveAmount(BigDecimal.ZERO));
        assertFalse(ValidationUtils.isPositiveAmount(null));
        assertFalse(ValidationUtils.isPositiveAmount(new BigDecimal("-1.00")));
    }

    @Test
    void testIsBalanceValid() {
        assertTrue(ValidationUtils.isBalanceValid(new BigDecimal("100.00"), new BigDecimal("100.00")));
        assertFalse(ValidationUtils.isBalanceValid(new BigDecimal("100.00"), new BigDecimal("99.00")));
        assertFalse(ValidationUtils.isBalanceValid(null, new BigDecimal("100.00")));
    }

    @Test
    void testIsValidPeriod() {
        assertTrue(ValidationUtils.isValidPeriod(2025, 1));
        assertTrue(ValidationUtils.isValidPeriod(2025, 12));
        assertFalse(ValidationUtils.isValidPeriod(2025, 13));
        assertFalse(ValidationUtils.isValidPeriod(2025, 0));
        assertFalse(ValidationUtils.isValidPeriod(null, 1));
    }

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertFalse(ValidationUtils.isValidEmail("invalid-email"));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    void testIsValidMobile() {
        assertTrue(ValidationUtils.isValidMobile("13800138000"));
        assertFalse(ValidationUtils.isValidMobile("12345678901"));
        assertFalse(ValidationUtils.isValidMobile(null));
    }

    @Test
    void testIsValidIdCard() {
        assertTrue(ValidationUtils.isValidIdCard("110101199003072345"));
        assertFalse(ValidationUtils.isValidIdCard("123456789012345678"));
        assertFalse(ValidationUtils.isValidIdCard(null));
    }
}
