package org.example.basic.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.NonNull;
import org.example.basic.annotations.Password;

@Builder
public record SignUpRequest(@NonNull String userName, @NonNull @Email String email,
                            @Password String password, @Password String confirmPassword) {
}
