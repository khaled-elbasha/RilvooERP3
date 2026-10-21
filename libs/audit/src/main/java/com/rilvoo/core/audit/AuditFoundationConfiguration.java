package com.rilvoo.core.audit;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the non-durable logging sink and publisher by default. A real application adds a
 * durable AuditSink bean (PostgreSQL/Kafka) and should then verify
 * {@code auditPublisher.hasDurableSink()} during startup checks.
 */
@Configuration(proxyBeanMethods = false)
public class AuditFoundationConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditSink.class)
    public LoggingAuditSink loggingAuditSink() {
        return new LoggingAuditSink();
    }

    @Bean
    @ConditionalOnMissingBean(AuditPublisher.class)
    public AuditPublisher auditPublisher(List<AuditSink> sinks) {
        return new AuditPublisher(sinks);
    }
}
