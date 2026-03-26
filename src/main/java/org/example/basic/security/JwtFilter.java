package org.example.basic.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.basic.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final UserDetailsService userService;

    private final HandlerExceptionResolver exceptionResolver;

    public JwtFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseToken(request);
            if (isValidToken(token)) authenticateUser(request, token);

            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();

            exceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private void authenticateUser(HttpServletRequest request, String token) {
        String userEmail = jwtProvider.getUserEmailFromToken(token);
        SecurityUser securityUser = (SecurityUser) userService.loadUserByUsername(userEmail);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String parseToken(HttpServletRequest request) {
        var cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (var cookie : cookies) {
            if ("access_token".equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isValidToken(String token) {
        return token != null && jwtProvider.isValidToken(token);
    }
}
