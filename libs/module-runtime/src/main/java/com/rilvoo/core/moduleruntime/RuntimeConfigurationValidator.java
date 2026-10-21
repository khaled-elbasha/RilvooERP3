package com.rilvoo.core.moduleruntime;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Startup gate that runs BEFORE any request is served:
 * <ul>
 *   <li>rejects unknown module ids (typo cannot silently disable a security-relevant API),</li>
 *   <li>rejects duplicates,</li>
 *   <li>rejects modules whose required capabilities the host does not provide,</li>
 *   <li>publishes the validated {@link EnabledModules} bean.</li>
 * </ul>
 */
public class RuntimeConfigurationValidator implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(RuntimeConfigurationValidator.class);

    private final RuntimeProperties properties;
    private final List<ModuleDescriptor> registeredDescriptors;
    private final Set<String> providedCapabilities;
    private final EnabledModulesHolder holder;

    public RuntimeConfigurationValidator(RuntimeProperties properties,
                                         List<ModuleDescriptor> registeredDescriptors,
                                         Set<String> providedCapabilities,
                                         EnabledModulesHolder holder) {
        this.properties = properties;
        this.registeredDescriptors = List.copyOf(registeredDescriptors);
        this.providedCapabilities = Set.copyOf(providedCapabilities);
        this.holder = holder;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, ModuleDescriptor> byId = new LinkedHashMap<>();
        for (ModuleDescriptor descriptor : registeredDescriptors) {
            if (byId.putIfAbsent(descriptor.id(), descriptor) != null) {
                throw new RuntimeConfigurationException("Duplicate module descriptor id: " + descriptor.id());
            }
        }

        List<String> requested = properties.getApis().getEnabled();
        Set<String> seen = new HashSet<>();
        for (String id : requested) {
            if (!seen.add(id)) {
                throw new RuntimeConfigurationException("Duplicate enabled module id: " + id);
            }
            ModuleDescriptor descriptor = byId.get(id);
            if (descriptor == null || !descriptor.available()) {
                throw new RuntimeConfigurationException(
                        "Unknown or unavailable API module '" + id + "'. Registered: " + byId.keySet());
            }
            Set<String> missing = new HashSet<>(descriptor.requiredCapabilities());
            missing.removeAll(providedCapabilities);
            if (!missing.isEmpty()) {
                throw new RuntimeConfigurationException(
                        "Module '" + id + "' requires capabilities the application does not provide: " + missing);
            }
        }

        List<ModuleDescriptor> enabled = requested.stream().map(byId::get).toList();
        holder.set(new EnabledModules(enabled));
        log.info("Runtime module activation validated: enabled={}", requested);
    }
}
