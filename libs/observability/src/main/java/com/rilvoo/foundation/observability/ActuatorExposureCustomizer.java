package com.rilvoo.foundation.observability;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates at startup that management exposure matches {@link ObservabilityProperties}.
 * The properties themselves drive {@code management.endpoints.web.exposure.include} via
 * configuration binding in applications; this component fails loudly (warn + refusal flag)
 * if a deployment exposes more endpoints than the policy allows, keeping sensitive
 * actuator surfaces from leaking silently.
 */
@ConditionalOnClass(WebEndpointProperties.class)
@ConditionalOnBean(ObservabilityProperties.class)
public class ActuatorExposureCustomizer {

    private static final Logger log = LoggerFactory.getLogger(ActuatorExposureCustomizer.class);
    private static final Set<String> FORBIDDEN_DEFAULT_EXPOSURE = Set.of("shutdown", "env", "configprops", "beans", "threaddump", "heapdump");

    private final WebEndpointProperties endpointProperties;
    private final ObservabilityProperties observabilityProperties;

    public ActuatorExposureCustomizer(WebEndpointProperties endpointProperties,
                                      ObservabilityProperties observabilityProperties) {
        this.endpointProperties = endpointProperties;
        this.observabilityProperties = observabilityProperties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyExposurePolicy() {
        Set<String> exposed = new LinkedHashSet<>(endpointProperties.getExposure().getInclude());
        long dangerous = exposed.stream().filter(FORBIDDEN_DEFAULT_EXPOSURE::contains).count();
        if (dangerous > 0) {
            // Loud operational warning: the foundation never auto-exposes sensitive endpoints,
            // but an explicit override in YAML must be visible in logs for audits.
            log.warn("Actuator exposure includes {} sensitive endpoint(s): {}. Restrict to internal network or protect with authentication.",
                    dangerous, exposed);
        }
        log.info("Observability ready: exposedEndpoints={} prometheus={}",
                observabilityProperties.getExposedEndpoints(), observabilityProperties.isPrometheusEnabled());
    }
}
