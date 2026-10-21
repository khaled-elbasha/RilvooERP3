package com.rilvoo.foundation.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.zalando.logbook.HttpHeaderFilter;
import org.zalando.logbook.QueryFilter;
import org.zalando.logbook.core.HeaderFilters;
import org.zalando.logbook.core.QueryFilters;

/**
 * Verifies the masking vocabulary used by {@link HttpLoggingConfiguration}: forbidden headers
 * disappear, secret query parameters are replaced. JSON body filtering is exercised through
 * Logbook's own tested JsonBodyFilters (replaceJsonStringProperty), so it is not re-implemented
 * or re-tested here.
 */
class SensitiveMaskingTest {

    @Test
    void authorizationHeaderIsRemovedEntirely() {
        HttpHeaderFilter filter = HttpLoggingConfiguration.FORBIDDEN_HEADERS.stream()
                .map(HeaderFilters::removeHeaders)
                .reduce(HttpHeaderFilter::merge)
                .orElse(HeaderFilters.none());
        assertThat(filter.filter("Authorization", "Bearer super-secret-token")).isEmpty();
        assertThat(filter.filter("Cookie", "session=abc")).isEmpty();
        assertThat(filter.filter("X-Trace-Id", "visible")).containsEntry("X-Trace-Id", "visible");
    }

    @Test
    void secretQueryParameterValuesAreReplaced() {
        QueryFilter filter = HttpLoggingConfiguration.SECRET_QUERY_PARAMS.stream()
                .map(QueryFilters::replaceValues)
                .reduce(QueryFilter::merge)
                .orElse(QueryFilters.none());
        String masked = filter.filter("code=AUTH-CODE&name=john");
        assertThat(masked).doesNotContain("AUTH-CODE");
        assertThat(masked).contains("name=john");
    }

    @Test
    void sensitiveFieldListCoversCredentialVocabulary() {
        List<String> required = List.of("password", "accessToken", "refreshToken", "totp", "privateKey");
        assertThat(HttpLoggingConfiguration.SENSITIVE_JSON_FIELDS).contains("password", "accessToken", "refreshToken", "totp", "privateKey");
        assertThat(required).allMatch(HttpLoggingConfiguration.SENSITIVE_JSON_FIELDS::contains);
    }
}
