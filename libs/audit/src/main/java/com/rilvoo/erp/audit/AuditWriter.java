package com.rilvoo.erp.audit;

import java.util.Map;
import java.util.UUID;

/** Port for append-only activity logging. Implementations must never throw into business flows. */
public interface AuditWriter {

    enum Outcome { SUCCESS, DENIED, FAILED }

    void record(UUID actorUserId, String action, String resource, String requestId,
                Outcome outcome, Map<String, Object> redactedDetails, String source);
}
