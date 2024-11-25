package com.aibees.service.maria.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CryptoUtils {

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[10];

        random.nextBytes(salt);

        return byteToString(salt);
    }

    /**
     * 단방향 Hashing 암호화 / MessageDigest
     * @param pwd
     * @param salt
     * @return
     */
    public static String EncryptPasswd(String pwd, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            digest.update((pwd+salt).getBytes());
            byte[] pwdSalt = digest.digest();

            return byteToString(pwdSalt);
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
            return null;
        }
    }

    private static String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
