package com.maulsinc.runescape;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.maulsinc.runescape.CommonsService.getDateAsString;
import static com.maulsinc.runescape.CommonsService.getDateAsUSString;
import static com.maulsinc.runescape.CommonsService.getFormattedNumber;
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
        String formattedNumber = getFormattedNumber(5000000L);

        assertEquals("5.000.000", formattedNumber);
    }

    @Test
    void testGetFormattedNumberNumberNull() {
        String formattedNumber = getFormattedNumber(null);

        assertEquals("--", formattedNumber);
    }

    @Test
    void testGetDateAsStringHappyFlow() {
        String dateAsString = getDateAsString(date);

        assertTrue(dateAsString.contains("11-10-2024"));
    }

    @Test
    void testGetDateAsStringNullDate() {
        String dateAsString = getDateAsString(null);

        assertFalse(StringUtils.hasText(dateAsString));
    }

    @Test
    void testGetDateAsUSStringHappyFlow() {
        String dateAsString = getDateAsUSString("11-10-2024 8:00 a.m. CEST");

        assertEquals("10-11-2024 8:00 a.m. CEST", dateAsString);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "Blub, ik ben een vis", "2024-10-11 10:00 AM UTC"})
    void testGetDateAsUSStringInvalidValues(String value) {
        assertFalse(StringUtils.hasText(getDateAsUSString(value)));
    }
}