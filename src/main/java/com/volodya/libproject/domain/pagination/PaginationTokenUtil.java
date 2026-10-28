package com.volodya.libproject.domain.pagination;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class PaginationTokenUtil {

    public static String encodeToken(String data) {
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeToken(String token) {
        return new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
    }
}
