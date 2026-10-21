package com.rilvoo.foundation.authorization;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

/**
 * Registers the fail-closed authorization wiring through Spring Boot's auto-configuration
 * mechanism instead of application component scanning.
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.security.core.Authentication")
@Import(AuthorizationFoundationConfiguration.class)
public class AuthorizationFoundationAutoConfiguration {
}
