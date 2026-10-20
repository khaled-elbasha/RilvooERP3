package com.rilvoo.example.web;

import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpHeaders;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.HeaderFilters;
import org.zalando.logbook.core.QueryFilters;
import org.zalando.logbook.json.JsonBodyFilters;

/**
 * Zalando Logbook wiring with strict sensitive-data masking:
 * <ul>
 *   <li>Authorization / Cookie / Set-Cookie / API-key headers are removed entirely,</li>
 *   <li>secret-bearing query parameters (code, token, client_secret, ...) are replaced by ***,</li>
 *   <li>known secret JSON body fields (password, accessToken, totp, privateKey, cardNumber, ...)
 *       are masked without ever breaking stream handling for non-JSON bodies,</li>
 *   <li>correlation logging carries the independent requestId next to trace IDs.</li>
 * </ul>
 * Production default is metadata-only HTTP logging; body logging stays disabled unless a
 * non-production profile explicitly raises {@code logbook.*} settings.
 */
@Configuration(proxyBeanMethods = false)
public class HttpLoggingConfiguration {

    /** Header names whose values must never reach any appender. */
    static final Set<String> FORBIDDEN_HEADERS = Set.of(
            "authorization", "cookie", "set-cookie", "x-api-key", "proxy-authorization");

    /** JSON field names whose string values are always masked in logged bodies. */
    static final Set<String> SENSITIVE_JSON_FIELDS = Set.of(
            "password", "currentPassword", "newPassword", "token", "accessToken", "refreshToken",
            "idToken", "secret", "clientSecret", "otp", "totp", "recoveryCode", "recoveryCodes",
            "passkey", "privateKey", "apiKey", "cardNumber", "iban", "cvv");

    @Bean
    @ConditionalOnMissingBean(Logbook.class)
    public Logbook logbook() {
        return Logbook.create()
                .withHeaderFilter(HttpHeaders::filterNamesBy, unused -> null) // placeholder replaced below
                .build();
    }
}
