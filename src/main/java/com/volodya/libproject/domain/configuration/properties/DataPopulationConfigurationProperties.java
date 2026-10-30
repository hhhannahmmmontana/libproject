package com.volodya.libproject.domain.configuration.properties;

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
    private DataPopulationStrategy strategy = DataPopulationStrategy.EMPTY_ONLY;
    private DataAmount dataAmount = new DataAmount();

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
