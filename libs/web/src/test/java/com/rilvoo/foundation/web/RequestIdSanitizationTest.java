package com.rilvoo.foundation.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.rilvoo.foundation.common.RequestIds;
import org.junit.jupiter.api.Test;

class RequestIdSanitizationTest {

    @Test
    void safeIdsArePreserved() {
        assertThat(RequestIds.sanitizeOrGenerate("order-7_x.Y-z")).isEqualTo("order-7_x.Y-z");
    }

    @Test
    void hostileIdsAreReplacedWithFreshUuid() {
        String replacement = RequestIds.sanitizeOrGenerate("evil\r\nX-Injected: 1");
        assertThat(replacement).doesNotContain("evil").matches("[0-9a-f-]{36}");
    }

    @Test
    void oversizedIdsAreReplaced() {
        assertThat(RequestIds.sanitizeOrGenerate("a".repeat(65))).hasSize(36);
    }
}
