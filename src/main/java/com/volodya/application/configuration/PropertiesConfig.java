package com.volodya.application.configuration;

import com.volodya.application.configurationproperties.DatabaseConfigurationProperties;
import com.volodya.application.configurationproperties.HeapDumpConfigurationProperties;
import com.volodya.application.configurationproperties.ServerConfigurationProperties;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource(value = "classpath:application-${profiles.active}.properties", ignoreResourceNotFound = true)
})
public abstract class PropertiesConfig {

    @Autowired
    private Environment env;

    private static final int DEFAULT_PORT = 8080;

    @Bean
    public ServerConfigurationProperties serverConfigurationProperties() {
        return new ServerConfigurationProperties(env.getProperty("server.port", Integer.class, DEFAULT_PORT));
    }

    @Bean
    public @Nullable HeapDumpConfigurationProperties heapDumpConfigurationProperties() {
        if (Objects.equals(env.getProperty("profiles.active"), "dev")) {
            return new HeapDumpConfigurationProperties(
                env.getProperty("heap-dump.file-path"),
                env.getProperty("heap-dump.live-only", Boolean.class, false)
            );
        } else {
            return null;
        }
    }

    @Bean
    public DatabaseConfigurationProperties databaseConfigurationProperties() {
        return new DatabaseConfigurationProperties(
            env.getProperty("db.type"),
            env.getProperty("db.driver.class-name"),
            env.getProperty("db.name"),
            env.getProperty("db.host"),
            env.getProperty("db.port"),
            env.getProperty("db.username"),
            env.getProperty("db.password")
        );
    }
}
