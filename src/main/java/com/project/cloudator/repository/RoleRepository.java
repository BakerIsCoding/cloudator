package com.project.cloudator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.cloudator.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
