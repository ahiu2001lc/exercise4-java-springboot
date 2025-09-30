package com.example.exercise4.mapper;

import com.example.exercise4.dto.request.UserRequest;
import com.example.exercise4.dto.response.UserResponse;
import com.example.exercise4.entity.UserEntity;
import com.example.exercise4.entity.UserRoleEntity;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Entity -> Response
    @Mapping(target = "roles", source = "userRoles")
    @Mapping(target = "enabled", expression = "java(entity.getEnabled() != null && entity.getEnabled() == 1)")
    UserResponse toResponse(UserEntity entity);

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(UserRequest request);

    // UserRoleEntity -> String (roleName)
    default Set<String> mapRoles(Set<UserRoleEntity> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return Set.of();
        }
        return userRoles.stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());
    }
}
