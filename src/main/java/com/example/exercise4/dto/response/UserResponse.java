package com.example.exercise4.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String fullName;
    Boolean enabled;
    LocalDateTime createdAt;
    Set<String> roles;
}
