package org.example.basic.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.SignUpRequest;
import org.example.basic.entities.Role;
import org.example.basic.entities.User;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.UserRepository;
import org.example.basic.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(SignUpRequest request) {
        validate(request);
        userRepository.save(buildUserEntity(request));
    }

    private void validate(SignUpRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_TAKEN);
        }
        if (userRepository.findByUserName(request.userName()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_TAKEN);
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    private User buildUserEntity(SignUpRequest request) {
        User user = new User();
        user.setUserName(request.userName());
        user.setEmail(request.email());
        user.setPasswordHashed(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        return user;
    }
}
