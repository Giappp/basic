package org.example.basic.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.User;
import org.example.basic.services.RefreshTokenService;
import org.example.basic.services.UserService;
import org.example.basic.utils.DeviceInfoUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public Oauth2SuccessHandler(JwtProvider jwtProvider, UserService userService, RefreshTokenService refreshTokenService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userService.processOauthUser(email, name);
        DeviceInfo deviceInfo = DeviceInfoUtil.getDeviceInfo(request);
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        response.setContentType("application/json");
        response.getWriter().write("""
                {
                "accessToken": "%s",
                "refreshToken": "%s"
                }
                """.formatted(accessToken, refreshToken));
    }
}
