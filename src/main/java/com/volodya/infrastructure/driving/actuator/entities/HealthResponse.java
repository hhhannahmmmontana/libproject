package com.volodya.infrastructure.driving.actuator.entities;

import java.util.Map;

public record HealthResponse(
    ApplicationStatus status,
    Map<String, Object> details
) {}
