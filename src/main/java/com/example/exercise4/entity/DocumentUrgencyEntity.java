package com.example.exercise4.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter @Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "document_urgency")
public class DocumentUrgencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "urgency_id")
    Long urgencyId;

    @Column(nullable = false, unique = true, length = 64)
    String name;

    @Column(name = "content")
    String content;

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
