package com.rilvoo.erp.authorization;

import java.util.Set;

/**
 * Port implemented by the IAM capability and consumed by every guarded use case.
 * Implementations MUST fail closed when authoritative state is inaccessible.
 */
public interface AccessEvaluator {

    /** Effective permission codes for the actor right now (GLOBAL + SELF scopes in Phase 1). */
    Set<String> effectivePermissions(ActorContext actor);

    /** True when the actor currently holds the permission (with revocation/status checks applied). */
    boolean hasPermission(ActorContext actor, String permissionCode);
}
