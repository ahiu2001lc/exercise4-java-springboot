package com.example.exercise4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private Set<String> roles;
}
