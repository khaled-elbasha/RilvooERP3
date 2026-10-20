package com.rilvoo.erp.authorization;

import java.util.Optional;
import java.util.UUID;

/**
 * Server-side view of the authenticated principal, resolved from a validated JWT plus the
 * authoritative IAM state in PostgreSQL. Never trust client-supplied user IDs.
 */
public record ActorContext(UUID userId, Optional<UUID> personId, String jwtId) {

    public boolean isSelf(UUID targetUserId) {
        return userId.equals(targetUserId);
    }
}
