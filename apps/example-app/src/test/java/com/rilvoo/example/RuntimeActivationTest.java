package com.rilvoo.example;

import static org.assertj.core.api.Assertions.assertThat;

import com.rilvoo.foundation.moduleruntime.ModuleDescriptor;
import com.rilvoo.foundation.moduleruntime.RuntimeConfigurationException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * libs:module-runtime must fail closed on unknown module ids so a typo can never silently
 * disable (or enable) a security-relevant API surface.
 */
class RuntimeActivationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(org.springframework.boot.autoconfigure.AutoConfigurations.of(
                    com.rilvoo.foundation.moduleruntime.ModuleRuntimeAutoConfiguration.class));

    @Test
    void unknownModuleIdRefusesStartup() {
        runner.withPropertyValues("app.runtime.apis.enabled=nonexistent-module")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure())
                            .hasRootCauseInstanceOf(RuntimeConfigurationException.class);
                });
    }

    @Test
    void emptyEnabledListStartsFoundationOnly() {
        runner.withPropertyValues("app.runtime.apis.enabled=")
                .run(context -> assertThat(context).hasNotFailed());
    }

    @Test
    void registeredModuleActivatesWhenCapabilityIsProvided() {
        runner.withPropertyValues("app.runtime.apis.enabled=demo")
                .withBean(DemoModule.class)
                .withBean(DemoCapabilities.class)
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context.getBean(com.rilvoo.foundation.moduleruntime.EnabledModules.class).isEnabled("demo")).isTrue();
                });
    }

    /** Test-only demonstration module; production code contains no placeholder modules. */
    static class DemoModule implements ModuleDescriptor {
        @Override
        public String id() {
            return "demo";
        }

        @Override
        public String displayName() {
            return "Demo";
        }

        @Override
        public Set<String> requiredCapabilities() {
            return Set.of("demo-capability");
        }
    }

    static class DemoCapabilities implements com.rilvoo.foundation.moduleruntime.ApplicationCapabilities {
        @Override
        public Set<String> provided() {
            return Set.of("demo-capability");
        }
    }
}
