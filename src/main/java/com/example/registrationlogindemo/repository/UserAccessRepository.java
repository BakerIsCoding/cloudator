package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {
    UserAccess findById(Integer id);

    @Query("UPDATE UserAccess u SET u.isblocked = :isblocked WHERE u.id = :id")
    @Modifying
    @Transactional
    public void updateIsBlocked(@Param("id") Long id, @Param("isblocked") boolean isblocked);

    @Query("UPDATE UserAccess u SET u.counter = :counter WHERE u.id = :id")
    @Modifying
    @Transactional
    public void updateCounter(@Param("id") Long id, @Param("counter") int counter);

    @Query(value = "SELECT u FROM UserAccess u WHERE u.id = :id")
    public UserAccess fetchUserAccess(@Param("id") Long id);

}
