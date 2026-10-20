package com.rilvoo.erp.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Uniform response envelope for every JSON success and handled-error response.
 * Exceptions: HTTP 204, binary payloads and /actuator/**.
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
