package com.rilvoo.foundation.observability;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configures observability helpers through Spring Boot's
 * {@code AutoConfiguration.imports} mechanism. Only active when Actuator classes are on the
 * classpath; requires no external systems (Prometheus server, Tempo, OTel collector) to start.
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties")
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ActuatorExposureCustomizer actuatorExposureCustomizer(
            org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties endpointProperties,
            ObservabilityProperties observabilityProperties) {
        return new ActuatorExposureCustomizer(endpointProperties, observabilityProperties);
    }
}
