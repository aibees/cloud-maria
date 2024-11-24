package com.aibees.service.maria.common.utils;

import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

    @Service
    public class AesUtil {

        private final static String STATUS = "status";
        private final static String RESULT = "result";
        private final static String PRD_KEY = "193507468nzexofn";
        private final static String DEV_KEY = "123456789neondev";

        public Map<String, Object> encryptTargetData(String env, String data) {
            String key = init(env, data);
            if(key.startsWith("FAILED"))
                return failResult(key);

            try {
                return successResult(env, encrypt(key, data));
            } catch(Exception e) {
                return failResult(e.toString());
            }
        }

        public Map<String, Object> decryptTargetData(String env, String data) {
            String key = init(env, data);
            if(key.startsWith("FAILED"))
                return failResult(key);

            try {
                return successResult(env, decrypt(key, data));
            } catch(Exception e) {
                return failResult(e.toString());
            }
        }

        private String init(String env, String data) {
            if(StringUtils.isNull(data)) {
                return "FAILED - NO DATA";
            }

            return ("DEV".equals(env.toUpperCase())) ? DEV_KEY : PRD_KEY;
        }

        private String encrypt(String key, String data) throws Exception {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            byte[] encrypted = cipher.doFinal(data.getBytes());

            return byteArrayToHex(encrypted);
        }

        private String decrypt(String key, String data) throws Exception {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] decrypted = cipher.doFinal(hexToByteArray(data));

            return new String(decrypted);
        }

        // translation byte <-> Hex
        private static byte[] hexToByteArray(String hex) {
            if (hex == null || hex.length() == 0) {
                return null;
            }
            byte[] ba = new byte[hex.length() / 2];
            for (int i = 0; i < ba.length; i++) {
                ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
            }
            return ba;
        }

        private static String byteArrayToHex(byte[] ba) {
            if (ba == null || ba.length == 0) {
                return null;
            }
            StringBuffer sb = new StringBuffer(ba.length * 2);
            String hexNumber;
            for (int x = 0; x < ba.length; x++) {
                hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
                sb.append(hexNumber.substring(hexNumber.length() - 2));
            }
            return sb.toString();
        }

        private Map<String, Object> failResult(String errmsg) {
            return ImmutableMap.of(STATUS, "FAILED", RESULT, errmsg);
        }

        private Map<String, Object> successResult(String env, String result) {
            return ImmutableMap.of(
                STATUS, "SUCCESS",
                "env", env,
                RESULT, result
            );
        }
    }