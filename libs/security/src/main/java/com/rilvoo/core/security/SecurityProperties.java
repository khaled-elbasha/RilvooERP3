package com.rilvoo.core.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized security settings under {@code app.security.*}. The JWT resource server is
 * configured through standard {@code spring.security.oauth2.resourceserver.jwt} properties so
 * any compliant OIDC issuer works without code changes; the values here drive claim
 * validation (issuer/audience/permission-claim) and the fail-fast production check in
 * {@link SecurityPropertiesValidator}.
 */
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /** Expected token issuer (should match spring.security.oauth2.resourceserver.jwt.issuer-uri). */
    private String jwtIssuer;

    /** Expected audience claim value for this application. */
    private String jwtAudience;

    /** Claim holding string permission codes mapped to authorities; blank disables the converter. */
    private String jwtPermissionClaim = "";

    /**
     * When true (default), startup fails if jwt-issuer or jwt-audience are missing. Only an
     * explicit dev/demo profile may set this to false — never weaken a production deployment.
     */
    private boolean failFastOnMissingJwtSettings = true;

    /** Paths reachable without authentication. Keep this list minimal and explicit. */
    private List<String> publicPaths = new ArrayList<>(List.of("/actuator/health/**", "/actuator/info"));

    /** Allowed CORS origins. Empty means CORS is disabled entirely (same-origin only). */
    private List<String> allowedOrigins = new ArrayList<>();

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public String getJwtAudience() {
        return jwtAudience;
    }

    public void setJwtAudience(String jwtAudience) {
        this.jwtAudience = jwtAudience;
    }

    public String getJwtPermissionClaim() {
        return jwtPermissionClaim;
    }

    public void setJwtPermissionClaim(String jwtPermissionClaim) {
        this.jwtPermissionClaim = jwtPermissionClaim;
    }

    public boolean isFailFastOnMissingJwtSettings() {
        return failFastOnMissingJwtSettings;
    }

    public void setFailFastOnMissingJwtSettings(boolean failFastOnMissingJwtSettings) {
        this.failFastOnMissingJwtSettings = failFastOnMissingJwtSettings;
    }

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
