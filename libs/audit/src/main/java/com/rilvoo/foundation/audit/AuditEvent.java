package com.rilvoo.foundation.audit;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Immutable audit record. Details are free-form but MUST be pre-sanitized: values matching
 * sensitive key names (password, token, secret, ...) are replaced at construction time so a
 * forgotten redaction cannot leak through this contract.
 */
public record AuditEvent(
        Instant occurredAt,
        NamedAuditAction action,
        String resourceType,
        String resourceId,
        AuditContext context,
        Map<String, Object> details) {

    private static final Pattern SENSITIVE_KEY = Pattern.compile(
            "(?i).*(password|token|secret|otp|totp|recovery|privatekey|apikey|cardnumber|cvv|iban).*");

    public static AuditEvent of(NamedAuditAction action, String resourceType, String resourceId, AuditContext context) {
        return new AuditEvent(Instant.now(), action, resourceType, resourceId, context, Map.of());
    }

    public AuditEvent withDetails(Map<String, Object> rawDetails) {
        return new AuditEvent(occurredAt, action, resourceType, resourceId, context, redact(rawDetails));
    }

    /** Recursively masks map keys that look sensitive; never mutates input maps. */
    public static Map<String, Object> redact(Map<String, Object> details) {
        if (details == null || details.isEmpty()) {
            return Map.of();
        }
        var copy = new java.util.LinkedHashMap<String, Object>();
        details.forEach((key, value) -> copy.put(key, SENSITIVE_KEY.matcher(key).matches() ? "***" : redactValue(value)));
        return Map.copyOf(copy);
    }

    @SuppressWarnings("unchecked")
    private static Object redactValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            return redact((Map<String, Object>) map);
        }
        if (value instanceof Set<?> set) {
            return set.stream().map(AuditEvent::redactValue).toList();
        }
        return value;
    }
}
