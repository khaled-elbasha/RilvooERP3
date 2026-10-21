package com.rilvoo.foundation.persistence;

/**
 * Contract for supplying request-scoped correlation values (acting user id, request id) to
 * persistence-layer integrations such as JPA auditing listeners or database session settings.
 * The foundation deliberately provides NO default implementation and opens NO database
 * connection: future applications bind this to their security context and, for PostgreSQL,
 * transaction-local {@code set_config('app.current_user_id', ..., true)} calls made only
 * inside an already-started transaction with server-side (never client-controlled) values.
 */
public interface CorrelationContext {

    /** Server-derived authenticated actor identifier, or {@code null} for system operations. */
    String currentUserId();

    /** Correlated request identifier for log/audit cross-referencing, may be {@code null}. */
    String requestId();
}
