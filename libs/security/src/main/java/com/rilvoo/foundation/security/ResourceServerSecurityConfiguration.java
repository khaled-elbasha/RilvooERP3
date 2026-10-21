package com.rilvoo.foundation.security;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Reusable, stateless OAuth2 Resource Server chain for APIs built on this foundation.
 * <ul>
 *   <li>Deny-by-default: only {@code app.security.public-paths} are open; business endpoints
 *       are NEVER permitAll.</li>
 *   <li>JWT signature, issuer and expiry validation come from the standard Boot resource
 *       server ({@code spring.security.oauth2.resourceserver.jwt.*}); audience validation is
 *       wired via {@link JwtValidationConfiguration} when configured.</li>
 *   <li>401/403 responses use the unified API envelope (security filters run outside
 *       {@code @RestControllerAdvice}, so they serialize themselves).</li>
 *   <li>No fake users, no hardcoded credentials, no default-secret token issuance anywhere.</li>
 * </ul>
 * The Authorization Server and WebAuthn remain cataloged options — never auto-configured here.
 * <p>Registered exclusively through Spring Boot auto-configuration
 * ({@code AutoConfiguration.imports}); never discovered by application component scanning.
 * {@code @ConditionalOnClass(SecurityFilterChain)} keeps the chain out of contexts without
 * Spring Security. Exactly one {@link SecurityFilterChain} bean is produced here; Boot's own
 * security auto-config backs off in its presence.</p>
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SecurityFilterChain.class)
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerSecurityConfiguration {

    /** Method-level security (@PreAuthorize ...) for applications built on this foundation. */
    @Configuration(proxyBeanMethods = false)
    @EnableMethodSecurity
    static class MethodSecurityConfiguration {
    }

    /** Fail-fast production check: refuses startup when required JWT settings are missing. */
    @Bean
    public static SecurityPropertiesValidator securityPropertiesValidator() {
        return new SecurityPropertiesValidator();
    }

    @Bean
    public SecurityFilterChain apiFilterChain(
            HttpSecurity http,
            SecurityProperties properties,
            JsonAuthenticationEntryPoint authenticationEntryPoint,
            JsonAccessDeniedHandler accessDeniedHandler) throws Exception {

        AntPathRequestMatcher[] publicMatchers = properties.getPublicPaths().stream()
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new);

        http
                .cors(cors -> {
                    if (!properties.getAllowedOrigins().isEmpty()) {
                        cors.configurationSource(corsConfigurationSource(properties));
                    } else {
                        cors.disable();
                    }
                })
                // Stateless bearer-token JSON API: no cookie-bearing state exists for CSRF to protect.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(publicMatchers).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationEntryPoint.class)
    public JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint(
            org.springframework.beans.factory.ObjectProvider<com.fasterxml.jackson.databind.ObjectMapper> objectProvider) {
        return new JsonAuthenticationEntryPoint(objectProvider.getIfAvailable(com.fasterxml.jackson.databind.ObjectMapper::new));
    }

    @Bean
    @ConditionalOnMissingBean(JsonAccessDeniedHandler.class)
    public JsonAccessDeniedHandler jsonAccessDeniedHandler(
            org.springframework.beans.factory.ObjectProvider<com.fasterxml.jackson.databind.ObjectMapper> objectProvider) {
        return new JsonAccessDeniedHandler(objectProvider.getIfAvailable(com.fasterxml.jackson.databind.ObjectMapper::new));
    }

    /** CORS is enabled only with an explicit allowlist; wildcard origins are rejected by policy. */
    private CorsConfigurationSource corsConfigurationSource(SecurityProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(properties.getAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Request-Id"));
        configuration.setExposedHeaders(List.of("X-Request-Id"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
