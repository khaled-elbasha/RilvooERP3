package com.rilvoo.erp.common;

/** Root of the framework-free exception vocabulary shared by all erp-systems layers. */
public abstract class ErpException extends RuntimeException {

    private final String errorCode;

    protected ErpException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ErpException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /** Stable machine-readable error code carried into the API error envelope. */
    public String errorCode() {
        return errorCode;
    }
}
