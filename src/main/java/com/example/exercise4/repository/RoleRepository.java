package com.example.exercise4.repository;

import com.example.exercise4.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);
}
