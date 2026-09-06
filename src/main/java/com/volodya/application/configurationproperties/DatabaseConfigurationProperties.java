package com.volodya.application.configurationproperties;

public record DatabaseConfigurationProperties(
    String dbType,
    String dbDriverClassName,
    String dbName,
    String dbHost,
    String dbPort,
    String dbUsername,
    String dbPassword
) {}
