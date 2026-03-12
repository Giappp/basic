package org.example.basic.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.*;
import org.example.basic.entities.Role;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.UserRepository;
import org.example.basic.security.JwtProvider;
import org.example.basic.security.SecurityUser;
import org.example.basic.utils.DeviceInfoUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse processSignIn(SignInRequest payload, HttpServletRequest request) {
        var securityUser = userService.loadUserByUsername(payload.email());
        DeviceInfo deviceInfo = DeviceInfoUtil.getDeviceInfo(request);
        log.info("Sign in request attempt for account ${} with Ip: ${}", securityUser.getUsername(), request.getRemoteAddr());
        if (isMatchesPassword(payload, securityUser)) {
            log.info("User {} successfully logged in with IP {}", securityUser.user().getId(), deviceInfo.ipv4());
            return buildToken(securityUser.user(), deviceInfo);
        }
        log.warn("Logged in attempted fail for user {} with IP {}", securityUser.user().getId(), deviceInfo.ipv4());
        throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    public void processSignUp(SignUpRequest request) {
        validate(request);
        userRepository.save(buildUserEntity(request));
        log.info("Created user with email {} successfully", request.email());
    }

    private User buildUserEntity(SignUpRequest request) {
        User user = new User();
        user.setUserName(request.userName());
        user.setEmail(request.email());
        user.setPasswordHashed(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        return user;
    }

    public TokenResponse processRefresh(RefreshTokenRequest payload, HttpServletRequest request) {
        String refreshToken = payload.refreshToken();
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
                .refreshToken(refreshTokenService.rotate(refreshToken, deviceInfo))
                .build();
    }

    private TokenResponse buildToken(User user, DeviceInfo deviceInfo) {
        return TokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user.getId(), deviceInfo))
                .build();
    }

    private boolean isMatchesPassword(SignInRequest request, SecurityUser user) {
        return passwordEncoder.matches(request.password(), user.getPassword());
    }

    private void validate(SignUpRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_INUSE);
        }
        if (userRepository.findByUserName(request.userName()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_TAKEN);
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
