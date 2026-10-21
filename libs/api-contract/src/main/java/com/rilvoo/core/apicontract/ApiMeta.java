package com.rilvoo.core.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/** Correlation metadata attached to every envelope. Timestamp is serialized as ISO-8601. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiMeta(String requestId, Instant timestamp, PaginationMeta pagination) {

    public static ApiMeta of(String requestId, Instant timestamp) {
        return new ApiMeta(requestId, timestamp, null);
    }

    public static ApiMeta paginated(String requestId, Instant timestamp, PaginationMeta pagination) {
        return new ApiMeta(requestId, timestamp, pagination);
    }
}
