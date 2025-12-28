package com.example.exercise4.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter @Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Long roleId;

    @Column(nullable = false, unique = true, length = 64)
    String name;

    @Column(name = "code", nullable = false)
    String roleCode;

    @Column(name = "description")
    String description;

    @Column(name = "address")
    String address;

    @Column(name = "enabled", nullable = false)
    Integer enabled = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "created_by")
    String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "del_flag")
    int delFlag;

    @Column(name = "deleted_by")
    String deletedBy;
}
