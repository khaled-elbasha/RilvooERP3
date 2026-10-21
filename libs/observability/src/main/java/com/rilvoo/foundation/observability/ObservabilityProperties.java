package com.rilvoo.foundation.observability;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code app.observability.*}. Controls which actuator endpoints the foundation
 * exposes and whether Prometheus metrics are enabled. Defaults are conservative:
 * health/info only; prometheus disabled unless explicitly turned on AND the endpoint is
 * placed behind authenticated infrastructure by the hosting application.
 */
@ConfigurationProperties(prefix = "app.observability")
public class ObservabilityProperties {

    /** Actuator web exposure allowlist (never include shutdown/env by default). */
    private List<String> exposedEndpoints = new ArrayList<>(List.of("health", "info"));

    /** When true, /actuator/prometheus is exposed in addition to the defaults above. */
    private boolean prometheusEnabled = false;

    public List<String> getExposedEndpoints() {
        return exposedEndpoints;
    }

    public void setExposedEndpoints(List<String> exposedEndpoints) {
        this.exposedEndpoints = exposedEndpoints;
    }

    public boolean isPrometheusEnabled() {
        return prometheusEnabled;
    }

    public void setPrometheusEnabled(boolean prometheusEnabled) {
        this.prometheusEnabled = prometheusEnabled;
    }
}
