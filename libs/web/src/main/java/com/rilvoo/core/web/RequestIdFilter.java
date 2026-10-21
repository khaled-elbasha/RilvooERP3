package com.rilvoo.core.web;

import com.rilvoo.core.common.RequestIds;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Establishes the request correlation id for every HTTP request:
 * <ul>
 *   <li>accepts {@code X-Request-Id} only when it matches a conservative safe pattern,
 *       otherwise generates a fresh UUID (never echoes hostile input),</li>
 *   <li>binds it to MDC ({@code requestId}) so logs and envelopes share one value,</li>
 *   <li>echoes it back on the response header,</li>
 *   <li>always clears MDC in a finally block (thread-pool hygiene).</li>
 * </ul>
 * It complements — never replaces — the independent traceId/spanId from Micrometer Tracing.
 */
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = RequestIds.sanitizeOrGenerate(request.getHeader(CorrelationKeys.REQUEST_ID_HEADER));
        MDC.put(CorrelationKeys.MDC_REQUEST_ID, requestId);
        response.setHeader(CorrelationKeys.REQUEST_ID_HEADER, requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CorrelationKeys.MDC_REQUEST_ID);
        }
    }
}
