package org.example.basic.services;

import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    String createRefreshToken(Long userId, DeviceInfo deviceInfo);

    User verify(String token);

    String rotate(String token, DeviceInfo deviceInfo);

    void invalidate(String token);
}
