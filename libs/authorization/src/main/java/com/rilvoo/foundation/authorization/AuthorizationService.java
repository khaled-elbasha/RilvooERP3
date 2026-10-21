package com.rilvoo.foundation.authorization;

/**
 * High-level authorization entry point for use cases: query or enforce effective permissions
 * through the configured {@link PermissionEvaluator}. Never grants anything implicitly.
 */
public class AuthorizationService {

    private final ActorContextResolver actorResolver;
    private final PermissionEvaluator evaluator;

    public AuthorizationService(ActorContextResolver actorResolver, PermissionEvaluator evaluator) {
        this.actorResolver = actorResolver;
        this.evaluator = evaluator;
    }

    public boolean hasPermission(String permission, PermissionScope scope, String scopeId) {
        return evaluator.hasPermission(actorResolver.resolve(), permission, scope, scopeId);
    }

    public void requirePermission(String permission, PermissionScope scope, String scopeId) {
        if (!hasPermission(permission, scope, scopeId)) {
            throw new PermissionDeniedException(permission);
        }
    }

    /** Convenience for SELF-scoped checks against the acting principal itself. */
    public void requireSelf(String permission) {
        CurrentActor actor = actorResolver.resolve();
        if (actor.anonymous() || !evaluator.hasPermission(actor, permission, PermissionScope.SELF, actor.actorId())) {
            throw new PermissionDeniedException(permission);
        }
    }
}
