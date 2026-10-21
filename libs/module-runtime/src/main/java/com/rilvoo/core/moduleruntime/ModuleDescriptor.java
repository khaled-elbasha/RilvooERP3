package com.rilvoo.core.moduleruntime;

import java.util.Set;

/**
 * Declarative description of a runtime API module (a Spring Modulith module exposing REST
 * interfaces). Modules declare themselves as beans; the runtime validates every enabled id
 * against registered descriptors and refuses unknown ids at startup (fail-closed).
 */
public interface ModuleDescriptor {

    /** Stable kebab-case identifier used in app.runtime.apis.enabled. */
    String id();

    /** Human-readable name for /actuator/info and diagnostics. */
    String displayName();

    /** Internal capability codes this module REQUIRES from the host application. */
    Set<String> requiredCapabilities();

    /** True when this module may be activated in the running application. */
    default boolean available() {
        return true;
    }
}
