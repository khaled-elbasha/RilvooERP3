package com.rilvoo.example.apicontract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Serialization tests for the unified envelope (null omission, shape of success/error/pagination). */
class ApiResponseSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void successEnvelopeOmitsNullErrorAndPagination() throws Exception {
        ApiResponse<Map<String, String>> response = ApiResponse.ok(Map.of("id", "42"),
                ApiMeta.of("req-1", Instant.parse("2026-10-21T10:00:00Z")));
        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"requestId\":\"req-1\""));
        assertFalse(json.contains("\"error\""));
        assertFalse(json.contains("\"pagination\""));
    }

    @Test
    void errorEnvelopeCarriesFieldValidationPaths() throws Exception {
        ApiError error = ApiError.validation("Request validation failed",
                Map.of("email", List.of("Invalid email address"), "address.postalCode", List.of("must not be blank")));
        ApiResponse<Void> response = ApiResponse.failure(error,
                ApiMeta.of("req-2", Instant.parse("2026-10-21T10:00:00Z")));
        String json = mapper.writeValueAsString(response);
        assertTrue(json.contains("\"success\":false"));
        assertTrue(json.contains("\"code\":\"VALIDATION_ERROR\""));
        assertTrue(json.contains("address.postalCode"));
        assertNull(mapper.readTree(json).get("data"));
    }

    @Test
    void paginationMetaComputesWindows() {
        PaginationMeta first = PaginationMeta.of(0, 20, 0);
        assertEquals(0, first.totalPages());
        assertFalse(first.hasNext());
        assertFalse(first.hasPrevious());

        PaginationMeta middle = PaginationMeta.of(1, 20, 45);
        assertEquals(3, middle.totalPages());
        assertTrue(middle.hasNext());
        assertTrue(middle.hasPrevious());
    }
}
