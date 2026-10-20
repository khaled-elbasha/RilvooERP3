package com.rilvoo.erp.apicontract;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/** Correlation metadata attached to every envelope. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiMeta(String requestId, Instant timestamp, PaginationMeta pagination) {
}
