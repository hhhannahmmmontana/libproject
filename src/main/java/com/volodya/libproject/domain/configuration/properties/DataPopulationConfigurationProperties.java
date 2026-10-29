package com.volodya.libproject.domain.configuration.properties;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "data-population")
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DataPopulationConfigurationProperties {

    private boolean enabled = false;
    private @Nullable DataAmount dataAmount = null;

    @PostConstruct
    public void init() {
        if (enabled && dataAmount == null) {
            dataAmount = new DataAmount();
        }
    }

    @Data
    public static class DataAmount {

        @Min(0)
        private int books = 2000;

        @Min(0)
        private int readers = 2000;

        @Min(0)
        private int bookLoans = 2000;
    }
}
