package com.rilvoo.foundation.authorization;

/**
 * Resolves the {@link CurrentActor} for the ongoing request. Implementations must derive the
 * actor exclusively from server-side security context — never from request parameters or
 * headers, which are client-controlled and untrusted.
 */
public interface ActorContextResolver {

    CurrentActor resolve();
}
