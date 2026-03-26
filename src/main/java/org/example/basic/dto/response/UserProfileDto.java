package org.example.basic.dto.response;

import lombok.Builder;

@Builder
public record UserProfileDto(String username, String email, String avatarUrl) {
}
