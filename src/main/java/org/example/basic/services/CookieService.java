package org.example.basic.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public interface CookieService {
    ResponseCookie createUserAccessTokenCookie(String accessToken);

    ResponseCookie createUserRefreshTokenCookie(String refreshToken);

    ResponseCookie cleanUserAccessTokenCookie();

    ResponseCookie cleanUserRefreshTokenCookie();

    ResponseCookie createAdminAccessTokenCookie(String accessToken);

    ResponseCookie createAdminRefreshTokenCookie(String refreshToken);

    ResponseCookie cleanAdminAccessTokenCookie();

    ResponseCookie cleanAdminRefreshTokenCookie();
}
