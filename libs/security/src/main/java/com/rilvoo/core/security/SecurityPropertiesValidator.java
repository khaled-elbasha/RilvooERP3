package com.rilvoo.core.security;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Fail-fast validation: when {@code app.security.fail-fast-on-missing-jwt-settings=true}
 * (the default, production-shaped configuration), JWT issuer and audience MUST be present or
 * startup fails. The template never silently weakens authentication; only an explicit
 * dev/demo profile may disable the check. Registered as a bean by
 * {@link ResourceServerSecurityConfiguration} via Spring Boot's Validator integration.
 */
public class SecurityPropertiesValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return SecurityProperties.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SecurityProperties properties = (SecurityProperties) target;
        if (properties.isFailFastOnMissingJwtSettings()) {
            if (isBlank(properties.getJwtIssuer())) {
                errors.rejectValue("jwtIssuer", "required",
                        "app.security.jwt-issuer must be configured when fail-fast is active");
            }
            if (isBlank(properties.getJwtAudience())) {
                errors.rejectValue("jwtAudience", "required",
                        "app.security.jwt-audience must be configured when fail-fast is active");
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
