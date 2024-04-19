package com.project.cloudator.repository;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.cloudator.entity.File;
import com.project.cloudator.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email la dirección de correo electrónico del usuario a buscar
     * @return el usuario encontrado o null si no se encuentra ninguno
     */
    User findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario del usuario a buscar
     * @return el usuario encontrado o null si no se encuentra ninguno
     */
    User findByUsername(String username);

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param id el ID del usuario a eliminar
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    public void deleteUser(@Param("userId") Long id);

    /**
     * Obtiene una lista de los primeros X usuarios.
     *
     * @param pageable la información de paginación para la consulta
     * @return una lista de usuarios
     */
    @Query(value = "SELECT u FROM User u")
    public List<User> findFirstXUsers(PageRequest pageable);

    /**
     * Actualiza el nombre de usuario de un usuario.
     *
     * @param userId      el ID del usuario a actualizar
     * @param newUsername el nuevo nombre de usuario
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :newUsername WHERE u.id = :userId")
    public void updateUsername(@Param("userId") Long userId, @Param("newUsername") String newUsername);

    /**
     * Actualiza la dirección de correo electrónico de un usuario.
     *
     * @param userId   el ID del usuario a actualizar
     * @param newEmail la nueva dirección de correo electrónico
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :userId")
    public void updateEmail(@Param("userId") Long userId, @Param("newEmail") String newEmail);

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param userId      el ID del usuario a actualizar
     * @param newPassword la nueva contraseña
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    public void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario del usuario a buscar
     * @return el usuario encontrado o null si no se encuentra ninguno
     */
    @Query(value = "SELECT u FROM User u where username=:user")
    public User fetchUser(@Param("user") String username);

}
