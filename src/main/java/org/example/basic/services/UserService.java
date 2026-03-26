package org.example.basic.services;

import org.example.basic.dto.SignUpRequest;
import org.example.basic.dto.response.UserProfileDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void createUser(SignUpRequest user);

    UserProfileDto getUserProfile(UserDetails userDetails);
}
