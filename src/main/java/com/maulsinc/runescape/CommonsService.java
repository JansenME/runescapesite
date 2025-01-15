package com.maulsinc.runescape;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CommonsService {
    public static final String COOKIE_NAME = "ownName";

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
        try {
            return new SimpleDateFormat(DATE_PATTERN_ALL).format(date);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String getDateAsUSString(String dateString) {
        if(!StringUtils.hasLength(dateString)) {
            return "";
        }

        try {
            Date date = new SimpleDateFormat(DATE_PATTERN_ALL).parse(dateString);
            return new SimpleDateFormat(DATE_PATTERN_US).format(date);
        } catch (ParseException | NullPointerException e) {
            log.error(String.format("Parsing date failed, date to parse was %s", dateString), e);
            return "";
        }
    }

    public static String replaceEmptySpace(String name) {
        name = name.replace(" ", "+");
        name = name.replace(" ", "+");
        return name;
    }

    public static String replacePlusToSpace(String name) {
        return name.replace("+", " ");
    }
}
