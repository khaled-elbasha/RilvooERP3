package com.rilvoo.erp.accesscatalog;

import java.util.Set;

/**
 * Role-grant scope vocabulary mirroring the {@code iam.user_roles.scope_type} CHECK constraint.
 * Phase 1 enforces only GLOBAL and SELF in access policies; ORGANIZATION/BRANCH/DEPARTMENT are
 * representable in storage but rejected by services with HTTP 422 until trusted ownership data exists.
 */
public enum GrantScopeType {
    GLOBAL,
    SELF,
    ORGANIZATION,
    BRANCH,
    DEPARTMENT;

    /** Scopes that Phase-1 authorization policies honor. */
    public static final Set<GrantScopeType> ENFORCEABLE_PHASE1 = Set.of(GLOBAL, SELF);

    /** Scopes storable but not yet enforceable — grants using them are refused at write time. */
    public static boolean isEnforceable(GrantScopeType type) {
        return ENFORCEABLE_PHASE1.contains(type);
    }
}
