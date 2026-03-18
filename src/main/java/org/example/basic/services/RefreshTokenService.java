package org.example.basic.services;

import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.RefreshToken;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.RefreshTokenRepository;
import org.example.basic.repositories.UserRepository;
import org.example.basic.security.SecurityUser;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final long refreshTokenExpirationSecond = 7 * 24 * 60 * 60;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(User user, DeviceInfo deviceInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationSecond));
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setIpv4Address(deviceInfo.ipv4());
        refreshToken.setDeviceInfo(deviceInfo.device());
        refreshTokenRepository.save(refreshToken);

        log.info("Create new session for user {} at ip {}", user.getId(), deviceInfo.ipv4());
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
        return createRefreshToken(user, deviceInfo);
    }

    public void invalidate(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        checkUser(refreshToken.getUser());
        log.info("Delete refresh token of user {}", refreshToken.getUser());
        refreshTokenRepository.delete(refreshToken);
    }

    private void checkUser(User user) {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.user().getId();
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
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
