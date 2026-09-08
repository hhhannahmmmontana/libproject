package com.volodya.application.configurationproperties;

public record ApplicationInfoConfigurationProperties(
    String name,
    String version,
    String commitHash,
    String buildTimestamp
) {}
