package com.project.cloudator.repository;

import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.UserRole;
import com.project.cloudator.entity.UserRoleId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    @Query("SELECT ur.role FROM UserRole ur WHERE ur.userId = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}