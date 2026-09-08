package com.volodya.infrastructure.driving.actuator.entities;

import java.util.Map;

public record HealthResponse(
    ApplicationStatus status,
    Map<String, Object> details
) {

    public static HealthResponse up(Map<String, Object> details) {
        return new HealthResponse(ApplicationStatus.UP, details);
    }

    public static HealthResponse down(Map<String, Object> details) {
        return new HealthResponse(ApplicationStatus.DOWN, details);
    }
}
