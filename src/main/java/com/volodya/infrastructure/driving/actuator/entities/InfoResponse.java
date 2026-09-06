package com.volodya.infrastructure.driving.actuator.entities;

import java.util.Map;

public record InfoResponse(
    Map<String, String> info
) {}
