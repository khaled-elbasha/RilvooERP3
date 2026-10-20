package com.rilvoo.erp.common;

/** A database-backed uniqueness/state conflict (SQLSTATE 23505 et al.). Maps to HTTP 409. */
public class ConflictException extends ErpException {

    public ConflictException(String message) {
        super("CONFLICT", message);
    }
}
