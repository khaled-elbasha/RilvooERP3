package com.rilvoo.core.common;

/**
 * Root of the framework-free exception vocabulary shared by every layer of applications built
 * on this foundation. Carries a stable machine-readable error code that global exception
 * handlers map into the standard API error envelope.
 */
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;

    protected ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /** Stable machine-readable error code carried into the API error envelope. */
    public String errorCode() {
        return errorCode;
    }
}
