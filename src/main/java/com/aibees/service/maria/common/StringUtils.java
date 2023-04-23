package com.aibees.service.maria.common;

public class StringUtils {

    public static boolean isNull(String str) {
        return (str == null || str.isEmpty());
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
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
}
