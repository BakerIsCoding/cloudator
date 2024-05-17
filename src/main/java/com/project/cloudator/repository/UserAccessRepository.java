package com.project.cloudator.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.UserAccess;
import java.util.List;

public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    /**
     * Busca un UserAccess por su ID.
     * 
     * @param id El ID del UserAccess a buscar.
     * @return El UserAccess encontrado, o null si no se encuentra.
     */
    UserAccess findById(Integer id);

    /**
     * Actualiza el estado de bloqueo de un UserAccess.
     * 
     * @param id        El ID del UserAccess a actualizar.
     * @param isblocked El nuevo estado de bloqueo.
     */
    @Query("UPDATE UserAccess u SET u.isblocked = :isblocked WHERE u.id = :id")
    @Modifying
    @Transactional
    public void updateIsBlocked(@Param("id") Long id, @Param("isblocked") boolean isblocked);

    /**
     * Actualiza el contador de un UserAccess.
     * 
     * @param id      El ID del UserAccess a actualizar.
     * @param counter El nuevo valor del contador.
     */
    @Query("UPDATE UserAccess u SET u.counter = :counter WHERE u.id = :id")
    @Modifying
    @Transactional
    public void updateCounter(@Param("id") Long id, @Param("counter") int counter);

    /**
     * Recupera un UserAccess por su ID.
     * 
     * @param id El ID del UserAccess a recuperar.
     * @return El UserAccess encontrado, o null si no se encuentra.
     */
    @Query(value = "SELECT u FROM UserAccess u WHERE u.id = :id")
    public UserAccess fetchUserAccess(@Param("id") Long id);

}
