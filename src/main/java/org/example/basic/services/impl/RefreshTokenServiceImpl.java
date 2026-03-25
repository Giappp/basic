package org.example.basic.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.RefreshToken;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.RefreshTokenRepository;
import org.example.basic.repositories.UserRepository;
import org.example.basic.security.SecurityUser;
import org.example.basic.services.RefreshTokenService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Value(value = "${security.refreshTokenExpiration}")
    private long refreshTokenExpirationSecond;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(Long userId, DeviceInfo deviceInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenExpirationSecond));
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setIpv4Address(deviceInfo.ipv4());
        refreshToken.setDeviceInfo(deviceInfo.device());
        refreshTokenRepository.save(refreshToken);

        log.info("Create new session for user {} at ip {}", userId, deviceInfo.ipv4());
        return refreshToken.getToken().toString();
    }

    public User verify(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        return refreshToken.getUser();
    }

    public String rotate(String token, DeviceInfo deviceInfo) {
        RefreshToken refreshToken = getRefreshToken(token);
        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        log.info("Delete and create new refresh token for user {} at ip {}", user.getId(), deviceInfo.ipv4());
        return createRefreshToken(user.getId(), deviceInfo);
    }

    public void invalidate(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        checkUser(refreshToken.getUser());
        log.info("Delete refresh token of user {}", refreshToken.getUser());
        refreshTokenRepository.delete(refreshToken);
    }

    private void checkUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        Long userId = securityUser.user().getId();
        if (!user.getId().equals(userId)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private @NonNull RefreshToken getRefreshToken(String token) {
        UUID uuidToken = UUID.fromString(token);
        RefreshToken refreshToken = refreshTokenRepository.findByToken(uuidToken)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isExpire()) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        return refreshToken;
    }


    @Transactional
    public void deleteByUserId(Long userId) {
        User user = getUserOrThrow(userId);
        userRepository.delete(user);
    }

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
