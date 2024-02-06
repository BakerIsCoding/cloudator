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
}
