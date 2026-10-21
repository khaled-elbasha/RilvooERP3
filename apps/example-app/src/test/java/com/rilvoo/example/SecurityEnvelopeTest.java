package com.rilvoo.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Filter-chain rejections must produce the unified JSON envelope — never HTML, never stack
 * traces — plus the X-Request-Id correlation header.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SecurityEnvelopeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedApiCallReturns401EnvelopeWithRequestId() throws Exception {
        mockMvc.perform(get("/api/v1/ping").header("X-Request-Id", "test-req-401"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("X-Request-Id", "test-req-401"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.meta.requestId").value("test-req-401"));
    }

    @Test
    void invalidBearerTokenIsRejectedAsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/ping").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHENTICATED"));
    }

    @Test
    void hostileRequestIdIsReplacedNotEchoed() throws Exception {
        mockMvc.perform(get("/api/v1/ping").header("X-Request-Id", "evil\r\nInjected: 1"))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> {
                    String returned = result.getResponse().getHeader("X-Request-Id");
                    org.assertj.core.api.Assertions.assertThat(returned)
                            .doesNotContain("evil")
                            .isNotBlank();
                });
    }
}
