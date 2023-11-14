package com.maulsinc.runescape;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonsService {
    private static final String DATE_PATTERN_ALL = "dd-M-yyyy h:mm a z";
    private static final String DATE_PATTERN_US = "M-dd-yyyy h:mm a z";

    private CommonsService() {
    }

    public static String getFormattedNumber(Long number) {
        if (number == null) {
            return "--";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }

    public static String getDateAsString(Date date) {
        return new SimpleDateFormat(DATE_PATTERN_ALL).format(date);
    }

    public static String getDateAsUSString(String dateString) {
        try {
            Date date = new SimpleDateFormat(DATE_PATTERN_ALL).parse(dateString);
            return new SimpleDateFormat(DATE_PATTERN_US).format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}
