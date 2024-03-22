package com.aibees.service.maria.common;

import com.aibees.service.maria.accountbook.util.AccConstant;

import java.util.Map;
import java.util.Objects;

public class MapUtils {

    public static String getString(Map<String, Object> map, String key) {
        if(Objects.isNull(map.get(key)))
            return AccConstant.EMPTY_STR;
        else
            return (String)map.get(key);
    }

    public static Integer getInteger(Map<String, Object> map, String key) {
        if(Objects.isNull(map.get(key)))
            return 0;
        else
            return Integer.parseInt(map.get(key).toString());
    }

    public static Long getLong(Map<String, Object> map, String key) {
        if(Objects.isNull(map.get(key)))
            return 0L;
        else
            return Long.parseLong(map.get(key).toString());
    }

    public static void printMap(Map<String, Object> map) {
        map.keySet().forEach(k -> { System.out.println("["+k+"]="+map.get(k)); });
    }
}
