package com.project.cloudator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.Role;

import jakarta.persistence.Entity;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Query(value = "SELECT r FROM Role r WHERE r.id = :id")
    Role fetchRoleById(@Param("id") Long id);
}
