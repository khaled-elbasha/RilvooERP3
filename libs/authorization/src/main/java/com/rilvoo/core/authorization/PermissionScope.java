package com.rilvoo.core.authorization;

/**
 * Scope of an effective permission grant. GLOBAL applies everywhere; ORGANIZATION/BRANCH/
 * DEPARTMENT require trusted ownership data before they may be honored by a concrete policy
 * (foundation stays database-independent and does not evaluate them); SELF always refers to
 * the acting principal itself.
 */
public enum PermissionScope {
    GLOBAL,
    ORGANIZATION,
    BRANCH,
    DEPARTMENT,
    SELF
}
