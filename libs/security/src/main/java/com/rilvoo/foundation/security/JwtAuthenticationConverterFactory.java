package com.rilvoo.foundation.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Converts a validated Jwt into authorities from a configurable claim path. Default claim is
 * {@code permissions}; concrete applications override the converter bean to map their own
 * scope/role claims. Authorities are prefixed with {@code SCOPE_}-free raw codes so method
 * security can use {@code hasAuthority("PERM:NAME")}.
 */
public final class JwtAuthenticationConverterFactory {

    private JwtAuthenticationConverterFactory() {
    }

    /** Builds a converter reading string authorities from the given top-level claim. */
    public static Converter<Jwt, AbstractAuthenticationToken> fromClaim(String claimName) {
        return jwt -> {
            Object raw = jwt.getClaims().get(claimName);
            Collection<GrantedAuthority> authorities = extractAuthorities(raw);
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    private static Collection<GrantedAuthority> extractAuthorities(Object raw) {
        List<String> values = switch (raw) {
            case List<?> list -> list.stream().map(String::valueOf).toList();
            case String single -> List.of(single.split("\\s+"));
            case Map<?, ?> ignored -> List.of();
            case null -> List.of();
            default -> List.of(String.valueOf(raw));
        };
        return values.stream()
                .filter(s -> !s.isBlank())
                .map(value -> (GrantedAuthority) () -> value)
                .toList();
    }
}
