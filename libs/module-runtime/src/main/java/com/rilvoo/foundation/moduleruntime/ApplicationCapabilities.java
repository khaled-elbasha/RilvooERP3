package com.rilvoo.foundation.moduleruntime;

import java.util.Set;

/**
 * Marker port: an application exposes the internal capability codes it provides (e.g.
 * "identity", "messaging"). Modules declare required capabilities in their descriptor and the
 * validator refuses activation when a capability is missing — no silent half-wired modules.
 */
public interface ApplicationCapabilities {

    Set<String> provided();
}
