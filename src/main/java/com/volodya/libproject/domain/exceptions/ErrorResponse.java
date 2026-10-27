package com.volodya.libproject.domain.exceptions;

import java.time.Instant;

public record ErrorResponse(
    String serviceName,
    String httpStatus,
    String errorName,
    String message,
    Instant timestamp
) {}
