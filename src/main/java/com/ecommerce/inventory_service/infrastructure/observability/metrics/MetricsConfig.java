package com.ecommerce.inventory_service.infrastructure.observability.metrics;

import io.micrometer.core.instrument.config.MeterFilter;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<?> meterRegistryCustomizer() {

        return registry -> registry.config()
                .commonTags(
                        "application",
                        "ecommerce",
                        "service",
                        "inventory-service"
                );
    }

    @Bean
    public MeterFilter metricsNamingConvention() {

        return MeterFilter.ignoreTags(
                "exception"
        );
    }
}