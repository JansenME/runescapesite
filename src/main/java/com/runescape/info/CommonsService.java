package com.runescape.info;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonsService {
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
        return new SimpleDateFormat("dd-M-yyyy h:mm a z").format(date);
    }
}
