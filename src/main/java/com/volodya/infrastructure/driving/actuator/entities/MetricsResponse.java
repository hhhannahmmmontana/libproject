package com.volodya.infrastructure.driving.actuator.entities;

import java.util.Map;

public record MetricsResponse(
    Map<String, Object> metrics
) {}
