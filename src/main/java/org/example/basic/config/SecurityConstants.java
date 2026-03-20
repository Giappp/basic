package org.example.basic.config;

public class SecurityConstants {
    private SecurityConstants() {
        /* This utility class should not be instantiated */
    }

    protected static final String[] BYPASS_ENDPOINTS = {
            "/api/healthcheck/health",
            "/api/v1/auth/**",
            "/api/v1/products/",
            "/api/v1/products/search",

            // Swagger endpoints (OpenAPI 3 with springdoc)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };
}
