package org.example.basic.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.ApiResponse;
import org.example.basic.dto.SignInRequest;
import org.example.basic.dto.SignUpRequest;
import org.example.basic.services.AuthService;
import org.example.basic.services.AuthTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthTokenService authTokenService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody @Valid SignInRequest payload, HttpServletRequest request) {
        var token = authService.signIn(payload, request);

        var accessTokenCookie = authTokenService.createAccessTokenCookie(token.accessToken());
        var refreshTokenCookie = authTokenService.createRefreshTokenCookie(token.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success("Login success"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signUp(@RequestBody @Valid SignUpRequest payload, HttpServletRequest request) {
        authService.signUp(payload);
        return ResponseEntity.created(URI.create(request.getRequestURI()))
                .body(ApiResponse.success(""));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletRequest request) {
        var token = authService.refresh(refreshToken, request);
        var accessTokenCookie = authTokenService.createAccessTokenCookie(token.accessToken());
        var refreshTokenCookie = authTokenService.createRefreshTokenCookie(token.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success("Refresh success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Logout success"));
    }
}
