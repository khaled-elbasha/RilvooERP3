package com.rilvoo.foundation.authorization;

import org.springframework.security.core.context.SecurityContextHolder;

/** Default resolver backed by the Spring Security {@code SecurityContextHolder}. */
public class SecurityContextActorResolver implements ActorContextResolver {

    @Override
    public CurrentActor resolve() {
        return CurrentActor.from(SecurityContextHolder.getContext().getAuthentication());
    }
}
