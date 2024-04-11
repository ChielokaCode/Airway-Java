package org.airway.airwaybackend.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

    @Getter
    @Data
    @Setter
    @Component
    @ConfigurationProperties(prefix = "seed")
    public class SeedProperties {
        private boolean enabled;
    }

