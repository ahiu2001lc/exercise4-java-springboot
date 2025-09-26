package com.example.exercise4.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username is not blank")
    private String username;

    @NotBlank(message = "Password is not blank")
    private String password;
}
