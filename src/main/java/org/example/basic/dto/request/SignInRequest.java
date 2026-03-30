package org.example.basic.dto.request;

import jakarta.validation.constraints.Email;
import lombok.NonNull;
import org.example.basic.annotations.Password;

public record SignInRequest(@NonNull @Email(message = "Invalid Email") String email,
                            @NonNull @Password String password) {
}
