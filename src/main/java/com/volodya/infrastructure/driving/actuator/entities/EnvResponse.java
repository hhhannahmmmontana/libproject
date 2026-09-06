package com.volodya.infrastructure.driving.actuator.entities;

import java.util.Map;

public record EnvResponse(
    Map<String, String> env
) {}
