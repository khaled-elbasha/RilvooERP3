package com.rilvoo.core.authorization;

/**
 * Decides whether an actor holds a permission under a scope. Implementations are supplied by
 * concrete applications (database-backed grants, Redis-cached lookups, ...). The foundation
 * ships only fail-closed defaults: without a real provider every check denies.
 */
@FunctionalInterface
public interface PermissionEvaluator {

    /** Fail-closed default used when no application evaluator is configured. */
    PermissionEvaluator DENY_ALL = (actor, permission, scope, scopeId) -> false;

    boolean hasPermission(CurrentActor actor, String permission, PermissionScope scope, String scopeId);
}
