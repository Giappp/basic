package org.example.basic.dto;

import jakarta.validation.constraints.Email;
import lombok.NonNull;
import org.example.basic.annotations.Password;
import org.example.basic.errors.Messages;

public record SignInRequest(@NonNull @Email(message = Messages.Validation.EMAIL) String email,
                            @NonNull @Password String password) {
}
