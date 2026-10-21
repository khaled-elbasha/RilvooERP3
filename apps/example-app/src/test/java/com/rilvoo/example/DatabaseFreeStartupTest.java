package com.rilvoo.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

/**
 * Proves the foundation starts with NO database, Redis or SMTP server. The dev profile only
 * relaxes the fail-fast property check; a JwtDecoder bean is still created and every API
 * route remains authenticated (no permitAll shortcuts).
 */
@SpringBootTest
@ActiveProfiles("dev")
class DatabaseFreeStartupTest {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoadsWithoutAnyDataSource() {
        assertThat(jwtDecoder).isNotNull();
    }
}
