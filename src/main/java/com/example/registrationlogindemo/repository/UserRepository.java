package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.User;

import jakarta.transaction.Transactional;

import java.util.List;
import com.example.registrationlogindemo.entity.User;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    public void deleteUser(@Param("userId") Long id);

    @Query(value = "SELECT u FROM User u")
    public List<User> findFirstXUsers(PageRequest pageable);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :newUsername WHERE u.id = :userId")
    public void updateUsername(@Param("userId") Long userId, @Param("newUsername") String newUsername);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :userId")
    public void updateEmail(@Param("userId") Long userId, @Param("newEmail") String newEmail);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    public void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    @Query(value = "SELECT u FROM User u where username=:user")
    public User fetchUser(@Param("user") String username);
    

}
