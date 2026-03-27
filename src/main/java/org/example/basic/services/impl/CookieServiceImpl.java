package org.example.basic.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.basic.properties.SecurityProperties;
import org.example.basic.services.CookieService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
    private final SecurityProperties properties;

    @Override
    public ResponseCookie createUserAccessTokenCookie(String accessToken) {
        return buildCookieToken(properties.userCookieProps().getAccessToken().getName(),
                accessToken,
                properties.userCookieProps().getAccessToken().getPath(),
                properties.userAccessMaxAge(),
                "Lax");
    }

    @Override
    public ResponseCookie createUserRefreshTokenCookie(String refreshToken) {
        return buildCookieToken(properties.userCookieProps().getRefreshToken().getName(),
                refreshToken,
                properties.userCookieProps().getRefreshToken().getPath(),
                properties.userRefreshMaxAge(),
                "Lax");
    }

    public ResponseCookie cleanUserAccessTokenCookie() {
        return buildCleanCookie(properties.userCookieProps().getAccessToken().getName(),
                properties.userCookieProps().getAccessToken().getPath());
    }

    public ResponseCookie cleanUserRefreshTokenCookie() {
        return buildCleanCookie(properties.userCookieProps().getRefreshToken().getName(),
                properties.userCookieProps().getRefreshToken().getPath());
    }

    public ResponseCookie createAdminAccessTokenCookie(String accessToken) {
        return buildCookieToken(properties.adminCookieProps().getAccessToken().getName(),
                accessToken,
                properties.adminCookieProps().getAccessToken().getPath(),
                properties.adminAccessMaxAge(),
                "Strict");
    }

    public ResponseCookie createAdminRefreshTokenCookie(String refreshToken) {
        return buildCookieToken(properties.adminCookieProps().getRefreshToken().getName(),
                refreshToken,
                properties.adminCookieProps().getRefreshToken().getPath(),
                properties.adminRefreshMaxAge(),
                "Strict");
    }

    public ResponseCookie cleanAdminAccessTokenCookie() {
        return buildCleanCookie(properties.adminCookieProps().getAccessToken().getName(),
                properties.adminCookieProps().getAccessToken().getPath());
    }

    public ResponseCookie cleanAdminRefreshTokenCookie() {
        return buildCleanCookie(properties.adminCookieProps().getRefreshToken().getName(),
                properties.adminCookieProps().getRefreshToken().getPath());
    }

    private ResponseCookie buildCookieToken(String cookieName, String value, String path, long maxAgeSeconds, String sameSite) {
        return ResponseCookie.from(cookieName, value)
                .httpOnly(true)
                .secure(properties.getCookie().isSecure())
                .path(path)
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .sameSite(sameSite)
                .build();
    }

    private ResponseCookie buildCleanCookie(String name, String path) {
        return ResponseCookie.from(name)
                .httpOnly(true)
                .secure(properties.getCookie().isSecure())
                .path(path)
                .maxAge(0)
                .build();
    }
}
