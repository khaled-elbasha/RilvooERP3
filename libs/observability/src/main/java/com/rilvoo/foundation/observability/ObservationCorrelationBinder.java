package com.rilvoo.foundation.observability;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.MDC;
import com.rilvoo.foundation.web.CorrelationKeys;

/**
 * Small helper that snapshots the current correlation context (requestId plus traceIds when
 * Micrometer Tracing populated MDC) for inclusion in structured events such as audit records
 * or outbound headers. Purely additive: absent tracing yields an empty trace section, so the
 * template runs identically with or without a tracing backend.
 */
public final class ObservationCorrelationBinder {

    private ObservationCorrelationBinder() {
    }

    public static Map<String, String> currentCorrelation() {
        Map<String, String> correlation = new LinkedHashMap<>();
        putIfPresent(correlation, CorrelationKeys.MDC_REQUEST_ID);
        putIfPresent(correlation, CorrelationKeys.MDC_TRACE_ID);
        putIfPresent(correlation, CorrelationKeys.MDC_SPAN_ID);
        return Map.copyOf(correlation);
    }

    private static void putIfPresent(Map<String, String> target, String key) {
        String value = MDC.get(key);
        if (value != null && !value.isBlank()) {
            target.put(key, value);
        }
    }
}
