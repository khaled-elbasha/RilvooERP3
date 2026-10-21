package com.rilvoo.foundation.moduleruntime;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires the fail-closed runtime module gate. */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RuntimeProperties.class)
public class ModuleRuntimeConfiguration {

    @Bean
    public EnabledModulesHolder enabledModulesHolder() {
        return new EnabledModulesHolder();
    }

    @Bean
    public RuntimeConfigurationValidator runtimeConfigurationValidator(
            RuntimeProperties properties,
            ObjectProvider<ModuleDescriptor> descriptors,
            ObjectProvider<ApplicationCapabilities> capabilities,
            EnabledModulesHolder holder) {
        List<ModuleDescriptor> registered = descriptors.orderedStream().toList();
        Set<String> provided = capabilities.orderedStream()
                .flatMap(c -> c.provided().stream())
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        return new RuntimeConfigurationValidator(properties, registered, provided, holder);
    }
}
