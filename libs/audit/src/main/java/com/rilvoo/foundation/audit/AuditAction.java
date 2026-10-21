package com.rilvoo.foundation.audit;

/**
 * Open vocabulary of auditable actions. Applications may add module-specific codes via
 * dedicated enums implementing {@link NamedAuditAction}; this enum covers the reusable
 * cross-cutting set so sinks can aggregate consistently.
 */
public enum AuditAction implements NamedAuditAction {
    CREATE,
    UPDATE,
    DELETE,
    SOFT_DELETE,
    RESTORE,
    READ_SENSITIVE,
    LOGIN,
    LOGOUT,
    TOKEN_ISSUE,
    TOKEN_REVOKE,
    PERMISSION_GRANT,
    PERMISSION_REVOKE,
    CONFIG_CHANGE,
    EXPORT;

    @Override
    public String code() {
        return name();
    }
}
