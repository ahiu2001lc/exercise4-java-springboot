package com.example.exercise4.mapper;

import com.example.exercise4.dto.request.RoleRequest;
import com.example.exercise4.dto.response.RoleResponse;
import com.example.exercise4.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    // Entity -> Response
    RoleResponse toResponse(RoleEntity entity);

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    RoleEntity toEntity(RoleRequest request);
}
