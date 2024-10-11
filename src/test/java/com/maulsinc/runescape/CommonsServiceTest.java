package com.maulsinc.runescape;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonsServiceTest {
    static Date date;

    @BeforeAll
    static void setup() {
        date = new GregorianCalendar(2024, Calendar.OCTOBER, 11).getTime();
    }

    @Test
    void testGetFormattedNumberHappyFlow() {
        String formattedNumber = CommonsService.getFormattedNumber(5000000L);

        assertEquals("5.000.000", formattedNumber);
    }

    @Test
    void testGetFormattedNumberNumberNull() {
        String formattedNumber = CommonsService.getFormattedNumber(null);

        assertEquals("--", formattedNumber);
    }

    @Test
    void testGetDateAsStringHappyFlow() {
        String dateAsString = CommonsService.getDateAsString(date);

        assertTrue(dateAsString.contains("11-10-2024"));
    }

    @Test
    void testGetDateAsStringNullDate() {
        String dateAsString = CommonsService.getDateAsString(null);

        assertFalse(StringUtils.hasText(dateAsString));
    }

    @Test
    void testGetDateAsUSStringHappyFlow() {
        String dateAsString = CommonsService.getDateAsUSString("11-10-2024 8:00 a.m. CEST");

        assertEquals("10-11-2024 8:00 a.m. CEST", dateAsString);
    }

    @Test
    void testGetDateAsUSStringNullDate() {
        String dateAsString = CommonsService.getDateAsUSString(null);

        assertFalse(StringUtils.hasText(dateAsString));
    }

    @Test
    void testGetDateAsUSStringInvalidDate() {
        String dateAsString = CommonsService.getDateAsUSString("Blub, ik ben een vis");

        assertFalse(StringUtils.hasText(dateAsString));
    }

    @Test
    void testGetDateAsUSStringWrongPatternDate() {
        String dateAsString = CommonsService.getDateAsUSString("2024-10-11 10:00 AM UTC");

        assertFalse(StringUtils.hasText(dateAsString));
    }
}