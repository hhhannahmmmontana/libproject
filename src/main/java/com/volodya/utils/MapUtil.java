package com.volodya.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MapUtil {

    @SafeVarargs
    public static <K, V> Map<K, V> orderedMap(Map.Entry<K, V>... entries) {
        Map<K, V> map = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
