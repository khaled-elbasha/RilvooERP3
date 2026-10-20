package com.rilvoo.erp.audit;

import java.util.UUID;

/**
 * Holds the server-trusted actor id and request id for the current thread so that JPA auditing
 * and audit writes never trust client-controlled values. Set by a servlet filter after
 * authentication, cleared in finally. Transaction-local PostgreSQL session variables
 * (app.current_user_id / app.request_id) are only set inside an active transaction from these values.
 */
public final class AuditRequestContext {

    private static final ThreadLocal<AuditRequestContext> CURRENT = new ThreadLocal<>();

    private final UUID actorUserId;
    private final String requestId;

    public AuditRequestContext(UUID actorUserId, String requestId) {
        this.actorUserId = actorUserId;
        this.requestId = requestId;
    }

    public static void set(AuditRequestContext context) {
        CURRENT.set(context);
    }

    /** Empty when no request context is bound (background jobs, startup). Never throws. */
    public static java.util.Optional<AuditRequestContext> current() {
        return java.util.Optional.ofNullable(CURRENT.get());
    }

    public static void clear() {
        CURRENT.remove();
    }

    public UUID actorUserId() {
        return actorUserId;
    }

    public String requestId() {
        return requestId;
    }
}
