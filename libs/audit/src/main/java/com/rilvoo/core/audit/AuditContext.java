package com.rilvoo.core.audit;

import java.util.Map;

/**
 * Ambient information attached to every audit event: who (actorId), where (requestId for
 * correlation with logs/traces) and from which channel. Immutable; built once per request by
 * an application-supplied resolver or manually in use cases.
 */
public record AuditContext(String actorId, String requestId, String source, Map<String, String> tags) {

    public static AuditContext of(String actorId, String requestId) {
        return new AuditContext(actorId, requestId, "api", Map.of());
    }

    public AuditContext withTag(String key, String value) {
        var tags = new java.util.LinkedHashMap<>(this.tags);
        tags.put(key, value);
        return new AuditContext(actorId, requestId, source, Map.copyOf(tags));
    }
}
