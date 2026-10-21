package com.rilvoo.core.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI foundation: a consistent bearer-JWT security scheme and grouping support for
 * future modules. SpringDoc documents only controllers actually registered in the
 * application context, so module-gated (disabled) APIs never appear in the spec.
 * Swagger UI itself is protected by the security filter chain (see libs/security).
 */
@Configuration(proxyBeanMethods = false)
public class OpenApiConfiguration {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenApiCustomizer bearerJwtSecurityCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components == null) {
                components = new Components();
                openApi.setComponents(components);
            }
            components.addSchemas("ApiError", new io.swagger.v3.oas.models.media.ObjectSchema()
                    .addProperty("code", new io.swagger.v3.oas.models.media.StringSchema())
                    .addProperty("message", new io.swagger.v3.oas.models.media.StringSchema()));
            components.addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));
            openApi.addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
        };
    }

    /** Default group covers all versioned APIs; applications add more groups per module. */
    @Bean
    public GroupedOpenApi defaultApiGroup() {
        return GroupedOpenApi.builder().group("default").pathsToMatch("/api/**").build();
    }
}
