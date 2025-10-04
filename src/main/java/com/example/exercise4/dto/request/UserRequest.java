package com.example.exercise4.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank(message = "Username name must not be blank")
    @Size(max = 64, message = "Username must be at most 64 characters")
    String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 64, message = "Password must be in range 8-64 characters")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "Password must contain at least one number and one special character"
    )
    String password;

    String fullName;

    Set<String> roles;
}
