package org.example.basic.controller;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ApiResponse;
import org.example.basic.dto.response.UserProfileDto;
import org.example.basic.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileDto userProfile = userService.getUserProfile(userDetails);
        return ResponseEntity
                .ok(ApiResponse.success(userProfile));
    }
}
