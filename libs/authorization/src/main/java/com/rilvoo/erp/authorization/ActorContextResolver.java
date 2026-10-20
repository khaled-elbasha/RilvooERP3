package com.rilvoo.erp.authorization;

import java.util.Optional;

/** Resolves the current request's {@link ActorContext}; empty when unauthenticated. */
public interface ActorContextResolver {

    Optional<ActorContext> currentActor();
}
