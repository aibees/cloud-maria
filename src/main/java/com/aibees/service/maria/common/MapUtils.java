package com.aibees.service.maria.common;

import java.util.Map;

public class MapUtils {

    public static String getString(Map<String, Object> map, String key) {
        return (String)map.get(key);
    }

    public static Integer getInteger(Map<String, Object> map, String key) {
        return Integer.parseInt(map.get(key).toString());
    }
}
