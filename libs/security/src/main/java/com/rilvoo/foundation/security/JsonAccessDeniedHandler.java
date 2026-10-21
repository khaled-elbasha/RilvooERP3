package com.rilvoo.foundation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rilvoo.foundation.apicontract.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/** 403 handler producing the standard API envelope. */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JsonAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ApiEnvelopeWriter.write(objectMapper, request, response,
                HttpServletResponse.SC_FORBIDDEN,
                ApiError.forbidden("You do not have permission to access this resource"));
    }
}
