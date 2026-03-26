package org.example.basic.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.RefreshToken;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.RefreshTokenRepository;
import org.example.basic.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    @Value(value = "${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationSecond;

    @Override
    @Transactional
    public String rotate(String oldToken, User user, DeviceInfo deviceInfo) {
        RefreshToken oldRefreshToken = getRefreshToken(oldToken);
        if (!oldRefreshToken.getIpv4Address().equals(deviceInfo.ipv4())) {
            log.warn("Token rotation requested from a different IP for user {}. Old: {}, New: {}",
                    user.getId(), oldRefreshToken.getIpv4Address(), deviceInfo.ipv4());
        }
        refreshTokenRepository.delete(oldRefreshToken);
        log.info("Deleted old refresh token and creating new one for user {}", user.getId());
        return buildAndSaveRefreshToken(user, deviceInfo);
    }

    @Override
    @Transactional
    public String createRefreshToken(User user, DeviceInfo deviceInfo) {
        return buildAndSaveRefreshToken(user, deviceInfo);
    }

    private String buildAndSaveRefreshToken(User user, DeviceInfo deviceInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenExpirationSecond));
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setIpv4Address(deviceInfo.ipv4());
        refreshToken.setDeviceInfo(deviceInfo.device());

        refreshTokenRepository.save(refreshToken);
        log.info("Created new refresh session for user {} at ip {}", user.getId(), deviceInfo.ipv4());
        return refreshToken.getToken().toString();
    }

    @Override
    public User verify(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        return refreshToken.getUser();
    }

    @Override
    @Transactional
    public void invalidate(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        refreshTokenRepository.delete(refreshToken);
        log.info("Invalidated refresh token of user {}", refreshToken.getUser().getId());
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("Deleted all active sessions for user {}", userId);
    }

    private RefreshToken getRefreshToken(String token) {
        UUID uuidToken;
        try {
            uuidToken = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(uuidToken)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isExpire()) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        return refreshToken;
    }
}
