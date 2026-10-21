package com.rilvoo.foundation.audit;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Registers the audit contract wiring (non-durable logging sink + publisher) through
 * Spring Boot's auto-configuration mechanism. A real durable sink supplied by the
 * application replaces the default via {@code @ConditionalOnMissingBean}.
 */
@AutoConfiguration
@Import(AuditFoundationConfiguration.class)
public class AuditFoundationAutoConfiguration {
}
