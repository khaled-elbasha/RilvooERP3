package com.rilvoo.erp.audit;

import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Writes redacted activity records into {@code audit.activity_log} in an independent transaction
 * so that a rolled-back business transaction still leaves its DENIED/FAILED trace.
 * Failures to write audit records are logged but never propagate into business flows.
 */
@Component
public class JpaAuditWriter implements AuditWriter {

    private static final Logger log = LoggerFactory.getLogger(JpaAuditWriter.class);

    private final ActivityLogRepository repository;

    public JpaAuditWriter(ActivityLogRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(UUID actorUserId, String action, String resource, String requestId,
                       Outcome outcome, Map<String, Object> redactedDetails, String source) {
        try {
            repository.save(new ActivityLogEntry(
                    actorUserId, action, resource, requestId, outcome.name(),
                    toJson(redactedDetails), source));
        } catch (RuntimeException ex) {
            // Never leak payloads or stack detail into responses; keep server-side log minimal.
            log.warn("Failed to persist audit entry action={} outcome={}", action, outcome);
        }
    }

    private static String toJson(Map<String, Object> details) {
        if (details == null || details.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128).append('{');
        boolean first = true;
        for (Map.Entry<String, Object> e : details.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(jsonString(e.getKey())).append(':').append(jsonString(String.valueOf(e.getValue())));
        }
        return sb.append('}').toString();
    }

    private static String jsonString(String raw) {
        StringBuilder out = new StringBuilder(raw.length() + 8).append('"');
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.append('"').toString();
    }
}
