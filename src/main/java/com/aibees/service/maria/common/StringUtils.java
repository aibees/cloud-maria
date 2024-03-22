package com.aibees.service.maria.common;

import com.google.common.base.Strings;

import java.util.Random;

public class StringUtils {

    public static boolean isNull(String str) {
        return (str == null || str.isEmpty());
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean isEquals(String s1, String s2) {
        if(s1 == null || s2 == null) {
            return false;
        }

        return s1.equals(s2);
    }

    public static String UuidNumberFormat(int num) {
        StringBuilder num_str = new StringBuilder(Integer.toString(num));
        int len = num_str.length();

        for(int i = 0; i < 6-len; i++) {
            num_str.insert(0, '0');
        }

        return num_str.toString();
    }

    public static String UuidNumberFormat(long num) {
        return UuidNumberFormat((int) num);
    }

    public static String createEmptyString() {
        return new String();
    }

    public static String getRandomStr(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++) {
            if(random.nextBoolean()) {
                sb.append((char)((int)(random.nextInt(26)) + 97));
            }
        }

        return sb.toString();
    }

    public static String dateStrParseToYMD(String ymd, String tokenizer) {
        if(isNumeric(ymd)) {
            ymd = DateUtils.convertDateCntToDateFormat(Integer.parseInt(ymd), tokenizer);
        }

        String[] splitted = ymd.split(tokenizer);
        String yyyy = splitted[0];
        String mm = Strings.padStart(splitted[1], 2, '0');
        String dd = Strings.padStart(splitted[2], 2, '0');
        return yyyy + mm + dd;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
