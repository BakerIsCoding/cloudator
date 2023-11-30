package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.User;

import java.util.List;
import com.example.registrationlogindemo.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "SELECT u FROM User u where username=:user")
    public User fetchUser(@Param("user") String username);

}
