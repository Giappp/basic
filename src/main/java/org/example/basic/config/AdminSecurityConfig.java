package org.example.basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfig {
}
