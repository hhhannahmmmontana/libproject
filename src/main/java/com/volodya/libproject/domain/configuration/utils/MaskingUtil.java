package com.volodya.libproject.domain.configuration.utils;

public final class MaskingUtil {

    private MaskingUtil() {}

    public static String maskData(String data) {
        return data.charAt(0) + "*****" + data.charAt(data.length() - 1);
    }
}
