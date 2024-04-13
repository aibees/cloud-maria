package com.aibees.service.maria.common;

import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT ="yyyy/MM/ddTHH:mm:ss";

    public static String convertDateCntToDateFormat(int dateCnt, String tokenizer) {
        LocalDate calculedDate = LocalDate.of(1900, Month.JANUARY, 1).plusDays(dateCnt-2);

        String y = Integer.toString(calculedDate.getYear());
        String m = Strings.padStart(Integer.toString(calculedDate.getMonth().getValue()), 2, '0');
        String d = Strings.padStart(Integer.toString(calculedDate.getDayOfMonth()), 2, '0');

        return String.join((StringUtils.isNull(tokenizer)) ? "" : tokenizer, y, m, d);
    }

    public static String convertDateCntToDateFormat(int dateCnt) {
        return convertDateCntToDateFormat(dateCnt, null);
    }

    public static String getTodayStr(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date today = new Date();
        return format.format(today);
    }

    public static String addMonthDate(String src, String form, int add) {
        LocalDate curDate = LocalDate.parse(src, DateTimeFormatter.ofPattern(form));
        LocalDate nextDate = curDate.plusMonths(add);

        String y = Integer.toString(nextDate.getYear());
        String m = Strings.padStart(Integer.toString(nextDate.getMonth().getValue()), 2, '0');

        return String.join(AccConstant.EMPTY_STR, y, m);
    }

    public static String getTodayStr() {
        return getTodayStr(DEFAULT_DATE_FORMAT);
    }
}
