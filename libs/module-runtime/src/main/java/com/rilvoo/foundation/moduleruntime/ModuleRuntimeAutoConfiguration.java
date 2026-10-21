package com.rilvoo.foundation.moduleruntime;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Registers the runtime module activation gate through Spring Boot's auto-configuration
 * mechanism. Startup fails closed on unknown/duplicate enabled module ids.
 */
@AutoConfiguration
@Import(ModuleRuntimeConfiguration.class)
public class ModuleRuntimeAutoConfiguration {
}
