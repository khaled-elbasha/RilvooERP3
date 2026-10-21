package com.rilvoo.core.moduleruntime;

import org.springframework.beans.factory.FactoryBean;

/**
 * Exposes the validated {@link EnabledModules} as an injectable bean produced by
 * {@link RuntimeConfigurationValidator}. Injecting EnabledModules anywhere guarantees the
 * validator already ran — there is no path to an unvalidated module state.
 */
public class EnabledModulesHolder implements FactoryBean<EnabledModules> {

    private volatile EnabledModules value;

    void set(EnabledModules enabledModules) {
        this.value = enabledModules;
    }

    @Override
    public EnabledModules getObject() {
        if (value == null) {
            throw new IllegalStateException("EnabledModules accessed before runtime validation completed");
        }
        return value;
    }

    @Override
    public Class<?> getObjectType() {
        return EnabledModules.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
