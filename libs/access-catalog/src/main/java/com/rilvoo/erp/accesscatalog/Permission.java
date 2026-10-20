package com.rilvoo.erp.accesscatalog;

/**
 * Canonical permission codes seeded by Flyway into {@code iam.permissions}.
 * The database is authoritative; this enum is the compile-time single source of truth for code strings.
 */
public enum Permission {
    IAM_USER_READ("IAM:USER:READ"),
    IAM_USER_CREATE("IAM:USER:CREATE"),
    IAM_USER_UPDATE("IAM:USER:UPDATE"),
    IAM_ROLE_ASSIGN("IAM:ROLE:ASSIGN"),
    IAM_ROLE_REVOKE("IAM:ROLE:REVOKE"),
    PERSON_PROFILE_READ("PERSON:PROFILE:READ"),
    PERSON_PROFILE_CREATE("PERSON:PROFILE:CREATE"),
    PERSON_PROFILE_UPDATE("PERSON:PROFILE:UPDATE"),
    PERSON_PROFILE_DELETE("PERSON:PROFILE:DELETE");

    private final String code;

    Permission(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static Permission fromCode(String code) {
        for (Permission p : values()) {
            if (p.code.equals(code)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown permission code: " + code);
    }
}
