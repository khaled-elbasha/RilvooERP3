package com.rilvoo.core.audit;

/** Contract for application-defined action vocabularies used with {@link AuditEvent}. */
public interface NamedAuditAction {
    String code();
}
