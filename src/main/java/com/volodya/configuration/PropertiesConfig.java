package com.volodya.configuration;

import com.volodya.application.configurationproperties.ServerConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource(value = "classpath:application-${profiles.active}.properties", ignoreResourceNotFound = true)
})
public class PropertiesConfig {

    @Autowired
    private Environment env;

    private static final int DEFAULT_PORT = 8080;

    @Bean
    public ServerConfigurationProperties serverConfigurationProperties() {
        return new ServerConfigurationProperties(env.getProperty("server.port", Integer.class, DEFAULT_PORT));
    }
}
