package com.rilvoo.foundation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rilvoo.foundation.apicontract.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/** 401 handler producing the standard API envelope instead of an HTML login redirect. */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JsonAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Never echo the exception message: it can contain token fragments.
        ApiEnvelopeWriter.write(objectMapper, request, response,
                HttpServletResponse.SC_UNAUTHORIZED,
                ApiError.unauthorized("Authentication is required to access this resource"));
    }
}
