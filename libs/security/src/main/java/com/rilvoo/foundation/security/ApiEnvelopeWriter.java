package com.rilvoo.foundation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rilvoo.foundation.apicontract.ApiError;
import com.rilvoo.foundation.apicontract.ApiMeta;
import com.rilvoo.foundation.apicontract.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.slf4j.MDC;

/**
 * Writes a unified {@link ApiResponse} JSON error envelope from servlet-level components
 * (security filters run outside @RestControllerAdvice, so they must serialize themselves).
 * Reuses the request id established by RequestIdFilter when present.
 */
final class ApiEnvelopeWriter {

    private static final String REQUEST_ID_MDC_KEY = "requestId";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private ApiEnvelopeWriter() {
    }

    static void write(ObjectMapper objectMapper, HttpServletRequest request, HttpServletResponse response,
                      int status, ApiError error) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        String requestId = MDC.get(REQUEST_ID_MDC_KEY);
        if (requestId == null || requestId.isBlank()) {
            Object attribute = request.getAttribute(REQUEST_ID_HEADER);
            requestId = attribute != null ? attribute.toString() : null;
        }
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiResponse<Void> body = ApiResponse.failure(error, ApiMeta.of(requestId, Instant.now()));
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
