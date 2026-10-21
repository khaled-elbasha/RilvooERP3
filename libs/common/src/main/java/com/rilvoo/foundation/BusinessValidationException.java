package com.rilvoo.foundation.common;

/**
 * Domain/business rule rejection that is not bean-validation shaped. Maps to HTTP 422.
 * Use for semantic rejections (unsupported scope, impossible state transitions), not for
 * malformed requests (those are 400 via validation).
 */
public class BusinessValidationException extends ApplicationException {

    public BusinessValidationException(String message) {
        super("BUSINESS_RULE_VIOLATION", message);
    }

    public BusinessValidationException(String code, String message) {
        super(code, message);
    }
}
