package com.qoobot.openaccounting.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * DateUtils单元测试
 *
 * @author openaccounting
 */
class DateUtilsTest {

    @Test
    void testNow() {
        assertNotNull(DateUtils.now());
    }

    @Test
    void testFormat() {
        String formatted = DateUtils.format(java.time.LocalDateTime.now());
        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void testParse() {
        String dateTimeStr = "2025-02-15 10:30:00";
        java.time.LocalDateTime dateTime = DateUtils.parse(dateTimeStr);
        assertNotNull(dateTime);
        assertEquals(2025, dateTime.getYear());
        assertEquals(2, dateTime.getMonthValue());
        assertEquals(15, dateTime.getDayOfMonth());
    }

    @Test
    void testFirstDayOfMonth() {
        LocalDate firstDay = DateUtils.firstDayOfMonth();
        assertEquals(1, firstDay.getDayOfMonth());
    }

    @Test
    void testLastDayOfMonth() {
        LocalDate lastDay = DateUtils.lastDayOfMonth();
        LocalDate now = LocalDate.now();
        assertEquals(now.getMonth(), lastDay.getMonth());
        assertEquals(now.lengthOfMonth(), lastDay.getDayOfMonth());
    }

    @Test
    void testCurrentPeriod() {
        String period = DateUtils.currentPeriod();
        assertNotNull(period);
        assertTrue(period.matches("\\d{6}"));
    }

    @Test
    void testCurrentYear() {
        int year = DateUtils.currentYear();
        assertEquals(LocalDate.now().getYear(), year);
    }

    @Test
    void testCurrentMonth() {
        int month = DateUtils.currentMonth();
        assertEquals(LocalDate.now().getMonthValue(), month);
    }

    @Test
    void testDaysBetween() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 2);
        long days = DateUtils.daysBetween(start, end);
        assertEquals(1, days);
    }
}
