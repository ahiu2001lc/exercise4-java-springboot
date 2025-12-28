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
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    Long documentId;

    @Column(name = "type_id", nullable = false)
    Long typeId;

    @Column(name = "code")
    String code;

    @Column(name = "content")
    String content;

    @Column(name = "stype_id")
    Long stypeId;

    @Column(name = "status_id")
    Long statusId;

    @Column(name = "signer_id")
    Long signerId;

    @Column(name = "urgency_id")
    Long urgencyId;

    @Column(name = "department_id", nullable = false)
    Long departmentId;

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
