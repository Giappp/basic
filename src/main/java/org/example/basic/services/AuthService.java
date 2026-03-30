package org.example.basic.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.basic.dto.request.SignInRequest;
import org.example.basic.dto.request.SignUpRequest;
import org.example.basic.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest payload, HttpServletRequest request);

    void signUp(SignUpRequest request);

    TokenResponse refresh(String refreshToken, HttpServletRequest request);

    void logout(String refreshToken);
}
