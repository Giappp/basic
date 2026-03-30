package org.example.basic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminLoginRequest(@NotBlank
                                @Size(max = 50)
                                String username,
                                @NotBlank
                                @Size(max = 100)
                                String password) {
}
