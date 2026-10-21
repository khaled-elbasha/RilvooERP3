package com.rilvoo.core.security;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Adds issuer + audience validation on top of the standard resource-server JWT decoder ONLY
 * when {@code app.security.jwt-issuer} is configured. This keeps the template database-free
 * and IdP-free while guaranteeing that a configured deployment validates iss/aud/exp with
 * Nimbus signature verification (asymmetric keys discovered from the issuer's JWKS endpoint).
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "app.security", name = "jwt-issuer")
public class JwtValidationConfiguration {

    @Bean
    public JwtDecoder jwtDecoder(SecurityProperties properties) {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(properties.getJwtIssuer());
        if (properties.getJwtAudience() != null && !properties.getJwtAudience().isBlank()) {
            String required = properties.getJwtAudience();
            // Default validator covers signature/expiry/issuer; add an aud claim check.
            var validator = JwtValidators.createDefaultWithIssuer(properties.getJwtIssuer());
            validator.addValidators(new JwtClaimValidator<List<String>>(
                    JwtClaimNames.AUD, audiences -> audiences != null && audiences.contains(required)));
            decoder.setJwtValidator(validator);
        } else {
            decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(properties.getJwtIssuer()));
        }
        return decoder;
    }

    /** Maps a string permission claim into authorities for hasAuthority() method security. */
    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "jwt-permission-claim")
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter(SecurityProperties properties) {
        return JwtAuthenticationConverterFactory.fromClaim(properties.getJwtPermissionClaim());
    }
}
