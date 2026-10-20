package com.rilvoo.erp.common;

/** Domain rule rejection that is not bean-validation-shaped. Maps to HTTP 422. */
public class BusinessValidationException extends ErpException {

    public BusinessValidationException(String message) {
        super("BUSINESS_RULE_VIOLATION", message);
    }

    public BusinessValidationException(String code, String message) {
        super(code, message);
    }
}
