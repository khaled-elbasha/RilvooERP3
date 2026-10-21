package com.rilvoo.foundation.authorization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fail-closed wiring: if an application supplies no {@link PermissionEvaluator}, everyone is
 * denied. There is deliberately no permit-all default and no fake grant store.
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationFoundationConfiguration {

    @Bean
    @ConditionalOnMissingBean(ActorContextResolver.class)
    public ActorContextResolver actorContextResolver() {
        return new SecurityContextActorResolver();
    }

    @Bean
    @ConditionalOnMissingBean(PermissionEvaluator.class)
    public PermissionEvaluator denyAllPermissionEvaluator() {
        return PermissionEvaluator.DENY_ALL;
    }

    @Bean
    @ConditionalOnMissingBean(AuthorizationService.class)
    public AuthorizationService authorizationService(
            ActorContextResolver actorResolver, PermissionEvaluator evaluator) {
        return new AuthorizationService(actorResolver, evaluator);
    }
}
