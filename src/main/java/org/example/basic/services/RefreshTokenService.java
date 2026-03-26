package org.example.basic.services;

import org.example.basic.dto.DeviceInfo;
import org.example.basic.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    String createRefreshToken(User user, DeviceInfo deviceInfo);

    User verify(String token);

    void invalidate(String token);

    String rotate(String oldToken, User user, DeviceInfo deviceInfo);
}
