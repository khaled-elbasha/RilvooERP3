package com.rilvoo.core.moduleruntime;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds {@code app.runtime.*}. Empty enabled list means NO business API modules are active —
 * the template default. Unknown or duplicate ids fail startup via RuntimeConfigurationValidator.
 */
@ConfigurationProperties(prefix = "app.runtime")
public class RuntimeProperties {

    private final Apis apis = new Apis();

    public Apis getApis() {
        return apis;
    }

    public static class Apis {
        /** Enabled API module ids, e.g. [iam, crm]. Validated against registered descriptors. */
        private List<String> enabled = new ArrayList<>();

        public List<String> getEnabled() {
            return enabled;
        }

        public void setEnabled(List<String> enabled) {
            this.enabled = enabled;
        }
    }
}
