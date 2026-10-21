package com.rilvoo.foundation.audit;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fan-out entry point used by services. Publishing never fails the business transaction:
 * sink errors are logged, not propagated. At least one sink must exist; otherwise startup
 * wiring (see LoggingAuditSink default) keeps a safe no-op-free configuration.
 */
public class AuditPublisher {

    private static final Logger log = LoggerFactory.getLogger(AuditPublisher.class);

    private final List<AuditSink> sinks;

    public AuditPublisher(List<AuditSink> sinks) {
        this.sinks = List.copyOf(sinks);
    }

    public void publish(AuditEvent event) {
        for (AuditSink sink : sinks) {
            try {
                sink.publish(event);
            } catch (RuntimeException ex) {
                // Never let audit delivery break the business operation; alert via logs/metrics.
                log.error("Audit sink {} failed for action {} on {}:{}",
                        sink.getClass().getSimpleName(), event.action().code(), event.resourceType(), event.resourceId(), ex);
            }
        }
    }

    public boolean hasDurableSink() {
        return sinks.stream().anyMatch(AuditSink::durable);
    }
}
