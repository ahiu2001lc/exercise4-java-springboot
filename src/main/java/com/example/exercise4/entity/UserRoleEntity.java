package com.example.exercise4.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_roles")
public class UserRoleEntity {

    @EmbeddedId
    UserRoleId id = new UserRoleId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    RoleEntity role;

    public UserRoleEntity(UserEntity user, RoleEntity role){
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user != null ? user.getId() : null, role != null ? role.getId() : null);
    }
}
