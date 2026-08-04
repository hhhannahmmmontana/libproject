package com.volodya.infrastructure.driving.dto.responses;

public record ErrorResponse(
    String message,
    String timestamp
) {}
