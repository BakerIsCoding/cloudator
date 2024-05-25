package com.project.cloudator.service;

import java.util.List;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserAccess;

public interface UserService {
    /**
     * Guarda un usuario.
     *
     * @param userDto El DTO del usuario que se quiere guardar.
     */
    void saveUser(UserDto userDto);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email El correo electrónico del usuario que se quiere buscar.
     * @return El usuario con el correo electrónico proporcionado.
     */
    User findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario que se quiere buscar.
     * @return El usuario con el nombre de usuario proporcionado.
     */
    User findByUsername(String username);

    /**
     * Encuentra los primeros usuarios.
     *
     * @param page La página de usuarios que se quiere obtener.
     * @param size El número de usuarios por página.
     * @return Una lista de los primeros X usuarios.
     */
    List<UserDto> findFirstXUsers(Integer page, Integer size);

    /**
     * Encuentra todos los usuarios.
     *
     * @return Una lista de todos los usuarios.
     */
    List<UserDto> findAllUsers();

    /**
     * Busca el acceso de un usuario por su ID.
     *
     * @param id El ID del usuario que se quiere buscar.
     * @return El acceso del usuario con el ID proporcionado.
     */
    UserAccess findById(Integer id);

    /**
     * Actualiza el nombre de usuario de un usuario.
     *
     * @param userid   El ID del usuario cuyo nombre de usuario se quiere
     *                 actualizar.
     * @param username El nuevo nombre de usuario.
     */
    void updateUsername(Long userid, String username);

    /**
     * Actualiza el correo electrónico de un usuario.
     *
     * @param userid El ID del usuario cuyo correo electrónico se quiere actualizar.
     * @param email  El nuevo correo electrónico.
     */
    void updateEmail(Long userid, String email);

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param userid   El ID del usuario cuya contraseña se quiere actualizar.
     * @param password La nueva contraseña.
     */
    void updatePassword(Long userid, String password);

    /**
     * Bloquea a un usuario.
     *
     * @param id El ID del usuario que se quiere bloquear.
     * @return Un booleano que indica si el usuario fue bloqueado con éxito.
     */
    boolean blockUser(Long id);

    /**
     * Desbloquea a un usuario.
     *
     * @param id El ID del usuario que se quiere desbloquear.
     * @return Un booleano que indica si el usuario fue desbloqueado con éxito.
     */
    boolean unBlockUser(Long id);

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id El ID del usuario que se quiere obtener.
     * @return El usuario con el ID proporcionado.
     */
    User getUserById(Long id);

    /**
     * Elimina a un usuario.
     *
     * @param id El ID del usuario que se quiere eliminar.
     */
    void deleteUser(Long id);

    /**
     * Busca un usuario.
     *
     * @param user El nombre de usuario o correo electrónico del usuario que se
     *             quiere buscar.
     * @return El usuario con el nombre de usuario o correo electrónico
     *         proporcionado.
     */
    User fetchUser(String user);

    /**
     * Obtiene el ID de un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario que se quiere buscar.
     * @return El ID del usuario con el nombre de usuario proporcionado.
     */
    Long getUserIdByUsername(String username);

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userid  El ID del usuario cuyo rol se quiere actualizar.
     * @param newRole El nuevo rol.
     */
    void updateRole(Long userid, Long newRole);

    /**
     * Obtiene los roles de un usuario por su ID.
     *
     * @param userId El ID del usuario cuyos roles se quieren obtener.
     * @return Una lista de los roles del usuario con el ID proporcionado.
     */
    List<String> getRolesByUserId(Long userId);

    /**
     * Obtiene el nombre de un usuario por su ID.
     *
     * @param userId El ID del usuario cuyo nombre se quiere obtener.
     * @return El nombre del usuario con el ID proporcionado.
     */
    String getUsernameById(Long userId);

}
