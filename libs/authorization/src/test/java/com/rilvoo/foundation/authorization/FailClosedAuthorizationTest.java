package com.rilvoo.foundation.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** The foundation must never grant permissions implicitly. */
class FailClosedAuthorizationTest {

    private final AuthorizationService service = new AuthorizationService(
            () -> CurrentActor.of("user-1", "User One"),   // authenticated actor
            PermissionEvaluator.DENY_ALL);                  // no provider configured => deny all

    @Test
    void denyAllEvaluatorRefusesEveryPermission() {
        assertThat(service.hasPermission("ANY:THING", PermissionScope.GLOBAL, null)).isFalse();
    }

    @Test
    void requirePermissionThrowsForMissingGrant() {
        assertThatThrownBy(() -> service.requirePermission("ANY:THING", PermissionScope.GLOBAL, null))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    void anonymousActorsCanNeverSatisfySelfChecks() {
        AuthorizationService anonymousService = new AuthorizationService(
                () -> CurrentActor.anonymous(),
                (actor, permission, scope, scopeId) -> true); // even a permissive evaluator must not help
        assertThatThrownBy(() -> anonymousService.requireSelf("SELF:READ"))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    void selfScopeUsesActorIdentityNotRequestParameters() {
        AuthorizationService scoped = new AuthorizationService(
                () -> CurrentActor.of("user-42", "User"),
                (actor, permission, scope, scopeId) ->
                        scope == PermissionScope.SELF && actor.actorId().orElse("").equals(scopeId));
        assertThat(scoped.hasPermission("X:READ", PermissionScope.SELF, "user-42")).isTrue();
        assertThat(scoped.hasPermission("X:READ", PermissionScope.SELF, "someone-else")).isFalse();
    }
}
