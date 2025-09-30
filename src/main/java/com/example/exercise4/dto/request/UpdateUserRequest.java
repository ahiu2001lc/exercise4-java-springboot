package com.example.exercise4.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String fullName;
    private Boolean enabled;
    private List<String> roles;
}
