package com.volodya.application.configuration;

import java.time.Instant;

public record ErrorResponse(
    String error,
    String message,
    Instant timestamp
) {

    public ErrorResponse(String error, String message) {
        this(error, message, Instant.now());
    }

    public static ErrorResponse fromException(Exception ex) {
        return new ErrorResponse(
            ex.getClass().getSimpleName(),
            ex.getMessage()
        );
    }
}
