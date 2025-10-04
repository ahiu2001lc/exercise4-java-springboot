package com.example.exercise4.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @NotBlank(message = "Role name must not be blank")
    @Size(max = 64, message = "Role name must be at most 64 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Role name must contain only uppercase letters and underscores")
    String name;
}
