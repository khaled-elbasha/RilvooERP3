package com.rilvoo.foundation.authorization;

import com.rilvoo.foundation.common.ForbiddenException;

/** Raised when the current actor lacks an effective permission. Maps to HTTP 403. */
public class PermissionDeniedException extends ForbiddenException {

    public PermissionDeniedException(String permission) {
        super("You do not have the required permission to perform this operation");
        // The requested permission code is intentionally NOT included in the client message;
        // log it server-side via AuthorizationService instead.
    }
}
