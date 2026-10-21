package com.rilvoo.foundation.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration entry point for the security foundation. Runs after Boot's own
 * {@link SecurityAutoConfiguration} so that exactly ONE {@code SecurityFilterChain}
 * (the one declared in {@link ResourceServerSecurityConfiguration}) is registered and
 * Boot's default chain backs off. Applications never need to import these beans manually.
 */
@AutoConfiguration(after = SecurityAutoConfiguration.class)
@ConditionalOnClass(name = "org.springframework.security.web.SecurityFilterChain")
@Import(ResourceServerSecurityConfiguration.class)
public class SecurityFoundationAutoConfiguration {
}
