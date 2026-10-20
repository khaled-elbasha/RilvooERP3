package com.rilvoo.example.apicontract;

import java.time.Instant;

/**
 * Reusable response factories. Every REST layer should build envelopes through this class so
 * the requestId/timestamp pairing and NON_NULL semantics stay uniform across applications.
 */
public final class ApiResponses {

    private ApiResponses() {
    }

    public static <T> ApiResponse<T> success(T data, String requestId) {
        return ApiResponse.ok(data, ApiMeta.of(requestId, Instant.now()));
    }

    public static ApiResponse<Void> success(String requestId) {
        return ApiResponse.ok(ApiMeta.of(requestId, Instant.now()));
    }

    public static <T> ApiResponse<java.util.List<T>> paginated(
            java.util.List<T> items, String requestId, int page, int size, long totalElements) {
        PaginationMeta pagination = PaginationMeta.of(page, size, totalElements);
        return ApiResponse.ok(items, ApiMeta.paginated(requestId, Instant.now(), pagination));
    }

    public static ApiResponse<Void> error(ApiError error, String requestId) {
        return ApiResponse.failure(error, ApiMeta.of(requestId, Instant.now()));
    }
}
