package com.example.exercise4.repository;

import com.example.exercise4.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FETCH u.userRoles ur " +
            "JOIN FETCH ur.role r " +
            "WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithRoles(@Param("username") String username);

    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    List<UserEntity> findAll();

    Page<UserEntity> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
