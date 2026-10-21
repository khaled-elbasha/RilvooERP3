package com.rilvoo.foundation.apicontract;

/** Page window description for list endpoints (zero-based pages). */
public record PaginationMeta(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {

    public static PaginationMeta of(int page, int size, long totalElements) {
        int totalPages = size <= 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return new PaginationMeta(
                page, size, totalElements, totalPages,
                (long) page + 1 < totalPages,
                page > 0);
    }
}
