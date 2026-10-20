package com.rilvoo.example.web;

/**
 * Single source of truth for correlation keys shared by the request-id filter, MDC,
 * Logbook and logging patterns. {@code requestId} is independent from tracing IDs:
 * traceId/spanId come from Micrometer Tracing (Brave) and may legitimately be absent.
 */
public final class CorrelationKeys {

    /** HTTP header used to propagate an externally generated request id. */
    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    /** MDC key holding the sanitized request id for the duration of a request. */
    public static final String MDC_REQUEST_ID = "requestId";

    /** MDC keys populated by Spring Boot's Micrometer Tracing/Brave auto-configuration. */
    public static final String MDC_TRACE_ID = "traceId";
    public static final String MDC_SPAN_ID = "spanId";

    private CorrelationKeys() {
    }
}
