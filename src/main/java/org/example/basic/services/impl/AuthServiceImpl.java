package org.example.basic.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.DeviceInfo;
import org.example.basic.dto.SignInRequest;
import org.example.basic.dto.SignUpRequest;
import org.example.basic.dto.response.TokenResponse;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.security.JwtUtil;
import org.example.basic.security.SecurityUser;
import org.example.basic.services.AuthService;
import org.example.basic.services.RefreshTokenService;
import org.example.basic.services.UserService;
import org.example.basic.utils.DeviceInfoUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse signIn(SignInRequest payload, HttpServletRequest request) {
        DeviceInfo deviceInfo = DeviceInfoUtil.getDeviceInfo(request);

        log.info("Sign in request attempt for account {} with Ip: {}", payload.email(), deviceInfo.ipv4());

        try {
            var securityUser = (SecurityUser) userDetailsService.loadUserByUsername(payload.email());

            if (isMatchesPassword(payload, securityUser)) {
                log.info("User {} successfully logged in with IP {}", securityUser.user().getId(), deviceInfo.ipv4());
                return buildToken(securityUser.user(), deviceInfo);
            }
        } catch (Exception ex) {
            log.warn("Login attempt failed (Not Found or Error) for email {} with IP {}", payload.email(), deviceInfo.ipv4());
        }

        throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    public void signUp(SignUpRequest request) {
        userService.createUser(request);
        log.info("Created user with email {} successfully", request.email());
    }

    public TokenResponse refresh(String refreshToken, HttpServletRequest request) {
        DeviceInfo deviceInfo = DeviceInfoUtil.getDeviceInfo(request);
        User user = refreshTokenService.verify(refreshToken);
        log.info("Rotate refresh token success for user {} at ip {}", user.getId(), deviceInfo.ipv4());
        return rotateToken(refreshToken, user, deviceInfo);
    }

    public void logout(String refreshToken) {
        refreshTokenService.invalidate(refreshToken);
        SecurityContextHolder.clearContext();
    }

    private TokenResponse rotateToken(String refreshToken, User user, DeviceInfo deviceInfo) {
        return TokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(refreshTokenService.rotate(refreshToken, user, deviceInfo))
                .build();
    }

    private TokenResponse buildToken(User user, DeviceInfo deviceInfo) {
        return TokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user, deviceInfo))
                .build();
    }

    private boolean isMatchesPassword(SignInRequest request, SecurityUser user) {
        return passwordEncoder.matches(request.password(), user.getPassword());
    }
}
