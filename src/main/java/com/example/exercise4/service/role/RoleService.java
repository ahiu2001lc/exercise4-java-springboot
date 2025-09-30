package com.example.exercise4.service.role;

import com.example.exercise4.dto.request.RoleRequest;
import com.example.exercise4.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse getRole(Long id);

    RoleResponse createRole(RoleRequest request);
}
