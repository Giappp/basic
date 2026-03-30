package org.example.basic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogoutRequest(@NotNull @NotBlank String refreshToken) {
}
