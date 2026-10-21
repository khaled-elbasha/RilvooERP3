package com.rilvoo.foundation.authorization;

import java.util.Optional;
import org.springframework.security.core.Authentication;

/**
 * Immutable view of the authenticated principal for authorization decisions. Framework-free
 * enough that services never touch {@link Authentication} directly. Anonymous requests map to
 * {@link #anonymous()} which carries no subject id and therefore can never satisfy SELF checks.
 */
public record CurrentActor(String actorId, String displayName, boolean anonymous) {

    public static final CurrentActor ANONYMOUS = new CurrentActor(null, null, true);

    public static CurrentActor anonymous() {
        return ANONYMOUS;
    }

    public static CurrentActor of(String actorId, String displayName) {
        if (actorId == null || actorId.isBlank()) {
            throw new IllegalArgumentException("actorId must not be blank");
        }
        return new CurrentActor(actorId, displayName, false);
    }

    public Optional<String> actorId() {
        return Optional.ofNullable(actorId);
    }

    /** Derives the actor from a Spring Security authentication, or anonymous when absent. */
    public static CurrentActor from(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ANONYMOUS;
        }
        return new CurrentActor(authentication.getName(), authentication.getName(), false);
    }
}
