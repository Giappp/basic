package org.example.basic.security;

public class SecurityConstants {
    public static final String[] BYPASS_ENDPOINTS = {
            "/api/healthcheck/**",
            "/api/v1/auth/**",
            "/oauth2/**",
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
