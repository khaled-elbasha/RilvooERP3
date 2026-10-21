package com.rilvoo.core.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

/**
 * Machine-readable error payload. Never contains stack traces, SQL, driver details,
 * credentials or raw rejected secret values. {@code fields} keys are dotted request paths
 * supporting nested validation (e.g. {@code "address.postalCode"}).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String code,
        String message,
        Map<String, List<String>> fields,
        Map<String, Object> details) {

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null, null);
    }

    public static ApiError validation(String message, Map<String, List<String>> fields) {
        return new ApiError("VALIDATION_ERROR", message, fields, null);
    }

    public static ApiError unauthorized(String message) {
        return new ApiError("UNAUTHENTICATED", message, null, null);
    }

    public static ApiError forbidden(String message) {
        return new ApiError("ACCESS_DENIED", message, null, null);
    }

    public static ApiError notFound(String message) {
        return new ApiError("NOT_FOUND", message, null, null);
    }

    public static ApiError conflict(String message) {
        return new ApiError("CONFLICT", message, null, null);
    }

    public static ApiError unprocessable(String code, String message) {
        return new ApiError(code, message, null, null);
    }

    public static ApiError internal() {
        return new ApiError("INTERNAL_ERROR", "An unexpected error occurred", null, null);
    }
}
