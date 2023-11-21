package com.aibees.service.maria.common;

import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.Month;

public class DateUtils {

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
}
