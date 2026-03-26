package org.example.basic.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public interface AuthTokenService {
    ResponseCookie createAccessTokenCookie(String accessToken);

    ResponseCookie createRefreshTokenCookie(String refreshToken);

    ResponseCookie createCleanCookie(String cookieName, String path);
}
