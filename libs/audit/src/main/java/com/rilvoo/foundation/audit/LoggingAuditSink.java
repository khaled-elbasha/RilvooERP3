package com.rilvoo.foundation.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default development sink: writes structured audit lines to SLF4J. Marked NON-durable so
 * nobody mistakes log output for an immutable audit trail.
 */
public class LoggingAuditSink implements AuditSink {

    private static final Logger log = LoggerFactory.getLogger("audit");

    @Override
    public void publish(AuditEvent event) {
        log.info("audit action={} resource={}#{} actor={} requestId={} details={}",
                event.action().code(), event.resourceType(), event.resourceId(),
                event.context().actorId(), event.context().requestId(), event.details());
    }

    @Override
    public boolean durable() {
        return false;
    }
}
