package org.example.basic.services;

import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.RefreshToken;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.RefreshTokenRepository;
import org.example.basic.repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final long refreshTokenExpirationSecond = 7 * 24 * 60 * 60;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(Long userId, DeviceInfo deviceInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationSecond));
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setIpv4Address(deviceInfo.ipv4());
        refreshToken.setDeviceInfo(deviceInfo.device());
        refreshTokenRepository.save(refreshToken);
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
        return createRefreshToken(user.getId(), deviceInfo);
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
