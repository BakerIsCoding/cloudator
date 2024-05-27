package com.project.cloudator.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

        /**
         * Actualiza la foto de un usuario.
         *
         * @param id   El ID del usuario cuya foto se quiere actualizar.
         * @param foto La nueva foto del usuario.
         */
        @Modifying
        @Transactional
        @Query("UPDATE UserInfo u SET u.foto = :foto WHERE u.id = :id")
        public void updatePcp(@Param("id") Long id, @Param("foto") String foto);

        /**
         * Busca la información de un usuario por su ID.
         *
         * @param id El ID del usuario que se quiere buscar.
         * @return La información del usuario con el ID proporcionado.
         */
        @Query(value = "SELECT u FROM UserInfo u WHERE u.id = :id")
        public UserInfo fetchUserInfo(@Param("id") Long id);
}
