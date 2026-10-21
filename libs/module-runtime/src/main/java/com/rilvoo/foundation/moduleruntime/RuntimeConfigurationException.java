package com.rilvoo.foundation.moduleruntime;

import com.rilvoo.foundation.common.ApplicationException;

/** Startup failure caused by invalid module configuration. Fail-closed by design. */
public class RuntimeConfigurationException extends ApplicationException {

    public RuntimeConfigurationException(String message) {
        super("RUNTIME_CONFIGURATION_INVALID", message);
    }
}
