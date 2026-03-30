package org.example.basic.services;

import org.example.basic.dto.request.AdminLoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface AdminAuthService {
    String login(AdminLoginRequest request, String ipAddress, String userAgent);
    
}
