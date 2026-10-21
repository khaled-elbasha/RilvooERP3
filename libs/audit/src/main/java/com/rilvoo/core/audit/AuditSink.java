package com.rilvoo.core.audit;

/**
 * Destination port for audit events. Implementations decide durability: PostgreSQL table,
 * Kafka topic, file... The foundation ships only a logging sink, which is explicitly NOT
 * durable business auditing — applications must register a real sink before claiming
 * compliance-grade audit trails.
 */
public interface AuditSink {

    void publish(AuditEvent event);

    /** True when this sink guarantees durable storage (logging sink returns false). */
    boolean durable();
}
