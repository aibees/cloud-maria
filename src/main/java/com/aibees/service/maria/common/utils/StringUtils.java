package com.aibees.service.maria.common.utils;

import com.aibees.service.maria.common.utils.DateUtils;
import com.google.common.base.Strings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class StringUtils {

    public static String EMPTY = "";

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

    public static boolean isNotEquals(String s1, String s2) {
        return !isEquals(s1, s2);
    }

    public static String getWithDefault(String s1, String defaultStr) {
        if(isNull(s1)) {
            return defaultStr;
        } else {
            return s1;
        }
    }

    public static String lpad(String str, int maxLen, String pad) {
        String newStr = "";
        for (int i = 0; i < maxLen-str.length(); i++) { newStr = newStr.concat(pad); }
        return newStr.concat(str);
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

    public static String generateRandomHash(int length) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(randomBytes);

        // Encode to Base64 to ensure it's readable and then trim to desired length
        String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

        if (hash.length() > length) {
            hash = hash.substring(0, length);
        } else {
            while (hash.length() < length) {
                hash += "0"; // Pad with zeros if the hash is shorter than desired length
            }
        }

        return hash;
    }
}
