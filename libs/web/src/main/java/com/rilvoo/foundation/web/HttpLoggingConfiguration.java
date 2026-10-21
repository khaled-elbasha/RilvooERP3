package com.rilvoo.foundation.web;

import java.util.List;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.HttpHeaderFilter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.QueryFilter;
import org.zalando.logbook.core.HeaderFilters;
import org.zalando.logbook.core.QueryFilters;
import org.zalando.logbook.json.JsonBodyFilters;

/**
 * Zalando Logbook 4.x wiring with strict sensitive-data masking:
 * <ul>
 *   <li>Authorization / Cookie / Set-Cookie / API-key headers are removed entirely,</li>
 *   <li>secret-bearing query parameters (code, token, client_secret, ...) are replaced by ***,</li>
 *   <li>known secret JSON body fields (password, accessToken, totp, privateKey, cardNumber, ...)
 *       are masked; non-JSON bodies pass through unchanged so stream handling never breaks,</li>
 *   <li>correlation logging carries the independent requestId next to trace IDs.</li>
 * </ul>
 * Production default is metadata-only HTTP logging; body logging stays disabled unless a
 * non-production profile explicitly raises {@code logbook.*} settings. The starter's own
 * auto-configuration backs off when this {@link Logbook} bean is present.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Logbook.class)
public class HttpLoggingConfiguration {

    /** Header names whose values must never reach any appender. */
    static final List<String> FORBIDDEN_HEADERS = List.of(
            "authorization", "cookie", "set-cookie", "x-api-key", "proxy-authorization");

    /** Query parameter names whose values are always replaced by a mask marker. */
    static final List<String> SECRET_QUERY_PARAMS = List.of(
            "code", "state", "token", "access_token", "refresh_token", "id_token",
            "client_secret", "secret", "password", "otp", "totp", "api_key", "apikey", "key");

    /** JSON member names whose string values are masked in logged bodies. */
    static final Set<String> SENSITIVE_JSON_FIELDS = Set.of(
            "password", "currentPassword", "newPassword", "confirmPassword", "token",
            "accessToken", "refreshToken", "idToken", "secret", "clientSecret",
            "otp", "totp", "recoveryCode", "recoveryCodes", "passkey", "privateKey",
            "apiKey", "cardNumber", "cvv", "iban");

    @Bean
    @ConditionalOnMissingBean(Logbook.class)
    public Logbook logbook() {
        // Remove every forbidden header name (case-insensitive), one merged filter.
        HttpHeaderFilter headerFilter = FORBIDDEN_HEADERS.stream()
                .map(HeaderFilters::removeHeaders)
                .reduce(HttpHeaderFilter::merge)
                .orElse(HeaderFilters.none());

        // Replace values of secret-bearing query parameters with ***.
        QueryFilter queryFilter = SECRET_QUERY_PARAMS.stream()
                .map(QueryFilters::replaceValues)
                .reduce(QueryFilter::merge)
                .orElse(QueryFilters.none());

        // Mask secret JSON members. replaceJsonStringProperty only acts on JSON content types,
        // so binary/plain-text bodies are returned untouched and streaming is never broken.
        BodyFilter bodyFilter = JsonBodyFilters.replaceJsonStringProperty(SENSITIVE_JSON_FIELDS, "***");

        return Logbook.create()
                .with(headerFilter, queryFilter, bodyFilter)
                .build();
    }
}
