package com.volodya.libproject.domain.pagination;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public final class PaginationTokenUtil {

    public static String encodeToken(String data) {
        log.trace("encoding data {}", data);
        var token = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(data.getBytes(StandardCharsets.UTF_8));
        log.trace("{} data encoded, got token {}", data, token);
        return token;
    }

    public static String decodeToken(String token) {
        log.trace("decoding token {}", token);
        var data = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
        log.trace("token {} decoded, got data {}", token, data);
        return token;
    }
}
