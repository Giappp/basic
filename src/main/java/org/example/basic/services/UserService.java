package org.example.basic.services;

import org.example.basic.dto.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void createUser(SignUpRequest user);
}
