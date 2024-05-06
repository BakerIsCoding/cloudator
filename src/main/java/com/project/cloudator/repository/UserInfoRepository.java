package com.project.cloudator.repository;

import jakarta.transaction.Transactional;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

        // FALTA AÑADIR LOS METODOS INDIVIDUALES
        // public void updateName()

        @Modifying
        @Transactional
        @Query("UPDATE UserInfo u SET u.foto = :foto WHERE u.id = :id")
        public void updatePcp(@Param("id") Long id, @Param("foto") String foto);

        @Query(value = "SELECT u FROM UserInfo u WHERE u.id = :id")
        public UserInfo fetchUserInfo(@Param("id") Long id);
}
