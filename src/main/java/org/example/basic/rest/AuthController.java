package org.example.basic.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ApiResponse;
import org.example.basic.dto.SignInRequest;
import org.example.basic.dto.SignUpRequest;
import org.example.basic.dto.TokenResponse;
import org.example.basic.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
        var token = authService.processSignIn(request);
        return ResponseEntity.ok(ApiResponse
                .<TokenResponse>builder()
                .data(token)
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest payload, HttpServletRequest request) {
        authService.processSignUp(payload);
        return ResponseEntity.created(URI.create(request.getRequestURI()))
                .body("Sign Up Success");
    }
}
