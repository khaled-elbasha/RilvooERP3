package com.rilvoo.erp.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

/** Machine-readable error payload. Never contains stack traces, SQL, credentials or raw tokens. */
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
}
