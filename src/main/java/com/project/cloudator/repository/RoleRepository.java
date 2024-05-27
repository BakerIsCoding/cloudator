package com.project.cloudator.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    /**
     * Busca un rol por su ID.
     *
     * @param id El ID del rol que se quiere buscar.
     * @return El rol con el ID proporcionado, o null si no se encuentra ningún rol
     *         con ese ID.
     */
    @Query(value = "SELECT r FROM Role r WHERE r.id = :id")
    Role fetchRoleById(@Param("id") Long id);

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId  El ID del usuario al que se le quiere cambiar el rol.
     * @param newRole El ID del nuevo rol que se le quiere asignar al usuario.
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserRole u SET u.roleId = :newRole WHERE u.userId = :userId")
    public void updateRole(@Param("userId") Long userId, @Param("newRole") Long newRole);
}