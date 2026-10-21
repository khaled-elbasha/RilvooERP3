package com.rilvoo.foundation.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Uniform response envelope for every JSON success and handled-error response.
 * Deliberate exceptions: HTTP 204 No Content, binary/streamed payloads and /actuator/**.
 * Null optional fields are omitted from serialization (Jackson NON_NULL).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, T data, ApiError error, ApiMeta meta) {

    public static <T> ApiResponse<T> ok(T data, ApiMeta meta) {
        return new ApiResponse<>(true, data, null, meta);
    }

    public static ApiResponse<Void> ok(ApiMeta meta) {
        return new ApiResponse<>(true, null, null, meta);
    }

    public static <T> ApiResponse<T> failure(ApiError error, ApiMeta meta) {
        return new ApiResponse<>(false, null, error, meta);
    }
}
