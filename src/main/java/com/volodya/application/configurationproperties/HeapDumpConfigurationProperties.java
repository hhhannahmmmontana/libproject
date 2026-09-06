package com.volodya.application.configurationproperties;

public record HeapDumpConfigurationProperties(
    String filePath,
    boolean liveOnly
) {}
