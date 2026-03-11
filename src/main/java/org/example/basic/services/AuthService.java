package org.example.basic.services;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.SignInRequest;
import org.example.basic.dto.SignUpRequest;
import org.example.basic.dto.TokenResponse;
import org.example.basic.entities.Role;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.UserRepository;
import org.example.basic.security.JwtProvider;
import org.example.basic.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserDetailsService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public TokenResponse processSignIn(SignInRequest request) {
        var user = userService.loadUserByUsername(request.email());
        if (isMatchesPassword(request, user)) {
            return buildToken((SecurityUser) user);
        }
        throw new AppException(ErrorCode.INVALID_CREDENTIALS);
    }

    public void processSignUp(SignUpRequest request) {
        validate(request);
        userRepository.save(buildUserEntity(request));
    }

    private User buildUserEntity(SignUpRequest request) {
        User user = new User();
        user.setUserName(request.userName());
        user.setEmail(request.email());
        user.setPasswordHashed(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        return user;
    }

    private TokenResponse buildToken(SecurityUser user) {
        return TokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(jwtProvider.generateRefreshToken(user))
                .build();
    }

    private boolean isMatchesPassword(SignInRequest request, UserDetails user) {
        return passwordEncoder.matches(request.password(), user.getPassword());
    }

    private void validate(SignUpRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_INUSE);
        }
        if (userRepository.findByUserName(request.userName()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_TAKEN);
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
