package org.example.basic.services.impl;

import org.example.basic.services.AuthTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieServiceImpl implements AuthTokenService {
    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${app.security.cookie.secure}")
    private boolean isSecure;

    @Override
    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(accessTokenExpiration / 1000)
                .sameSite("Lax")
                .build();
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/api/v1/auth/refresh")
                .maxAge(refreshTokenExpiration)
                .sameSite("Lax")
                .build();
    }

    @Override
    public ResponseCookie createCleanCookie(String cookieName, String path) {
        return ResponseCookie.from(cookieName)
                .httpOnly(true)
                .secure(isSecure)
                .path(path)
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
