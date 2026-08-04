package com.volodya.application.configuration;

import java.time.Instant;

public record ErrorResponse(
    String applicationName,
    String error,
    String message,
    Instant timestamp
) {

    public ErrorResponse(String applicationName, String error, String message) {
        this(applicationName, error, message, Instant.now());
    }

    public static ErrorResponse fromException(String applicationName, Exception ex) {
        return new ErrorResponse(
            applicationName,
            ex.getClass().getSimpleName(),
            ex.getMessage()
        );
    }
}
