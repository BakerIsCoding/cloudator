package com.project.cloudator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserAccess;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.repository.RoleRepository;
import com.project.cloudator.repository.UserAccessRepository;
import com.project.cloudator.repository.UserRepository;
import com.project.cloudator.repository.UserRoleRepository;
import com.project.cloudator.service.UserAccessService;
import com.project.cloudator.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAccessRepository userAccessRepository;

    @Autowired
    private LogWriter logWriter;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return El usuario correspondiente al ID especificado.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param user Nombre de usuario.
     * @return El usuario correspondiente al nombre de usuario especificado.
     */
    public User fetchUser(String user) {
        return userRepository.fetchUser(user);
    }

    /**
     * Obtiene el ID de un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario del que se quiere obtener el ID.
     * @return El ID del usuario.
     */
    public Long getUserIdByUsername(String username) {
        User user = userRepository.fetchUser(username);
        return user.getId();
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param userDto Datos del usuario a guardar.
     */
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        UserAccess userAccess = new UserAccess();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Encriptamos la contraseña
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // SE DEBEN MIGRAR LOS ROLES A /ADMIN/ROLES PARA NO HACER LA COMPROBACIÓN
        // SIEMPRE
        // Definimos los roles
        String roleName = "ROLE_SUPERADMIN";
        String role1Name = "ROLE_ADMIN";
        String role2Name = "ROLE_PREMIUM";
        String role3Name = "ROLE_USER";

        // Asignamos la respuesta de la base de datos a una variable tipo Role
        Role role = roleRepository.findByName(roleName);
        Role role1 = roleRepository.findByName(role1Name);
        Role role2 = roleRepository.findByName(role2Name);
        Role role3 = roleRepository.findByName(role3Name);

        // Comprovamos que existan los roles
        if (role == null) {
            role = checkRoleExist(roleName);
        }
        if (role1 == null) {
            role1 = checkRoleExist(role1Name);
        }
        if (role2 == null) {
            role2 = checkRoleExist(role2Name);
        }
        if (role3 == null) {
            role3 = checkRoleExist(role3Name);
        }

        // Asignamos al usuario el rol predeterminado, en nuestro caso se encuentra en
        // la variable roleName
        user.setRoles(Arrays.asList(role3));

        // Guardamos el usuario final en la base de datos
        userRepository.save(user);

        // Asignamos UserAccess
        User username = userRepository.fetchUser(userDto.getUsername());
        userAccess.setId(username.getId());

        userAccessRepository.save(userAccess);

    }

    /**
     * Obtiene los datos de acceso de un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Los datos de acceso del usuario correspondiente al ID especificado.
     */
    @Override
    public UserAccess findById(Integer id) {
        return userAccessRepository.findById(id);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return El usuario correspondiente al nombre de usuario especificado.
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email Dirección de correo electrónico.
     * @return El usuario correspondiente a la dirección de correo electrónico
     *         especificada.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Una lista de objetos UserDto que representan a los usuarios
     *         registrados.
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los primeros X usuarios registrados en el sistema.
     *
     * @param page Número de página.
     * @param size Cantidad de usuarios a mostrar.
     * @return Una lista de objetos UserDto que representan a los primeros X
     *         usuarios registrados.
     */
    @Override
    public List<UserDto> findFirstXUsers(Integer page, Integer size) {
        PageRequest pagination = PageRequest.of(page, size);
        List<User> users = userRepository.findFirstXUsers(pagination);

        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Convierte un objeto User a un objeto UserDto.
     *
     * @param user Objeto User a convertir.
     * @return El objeto UserDto resultante.
     */
    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    /**
     * Comprueba si un rol existe en la base de datos y lo guarda si no existe.
     *
     * @param individualRole Nombre del rol a comprobar.
     * @return El objeto Role existente o recién guardado.
     */
    private Role checkRoleExist(String individualRole) {
        Role role = new Role();
        role.setName(individualRole);
        return roleRepository.save(role);
    }

    /**
     * Actualiza el nombre de usuario de un usuario.
     *
     * @param userId   ID del usuario a actualizar.
     * @param username Nuevo nombre de usuario.
     */
    @Override
    public void updateUsername(Long userId, String username) {
        userRepository.updateUsername(userId, username);
    }

    /**
     * Actualiza la dirección de correo electrónico de un usuario.
     *
     * @param userId ID del usuario a actualizar.
     * @param email  Nueva dirección de correo electrónico.
     */
    @Override
    public void updateEmail(Long userId, String email) {
        userRepository.updateEmail(userId, email);
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param userId   ID del usuario a actualizar.
     * @param password Nueva contraseña.
     */
    @Override
    public void updatePassword(Long userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(userId, encodedPassword);
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId  El ID del usuario al que se le va a cambiar el rol.
     * @param newRole El nuevo rol que se le asignará al usuario.
     */
    @Override
    public void updateRole(Long userId, Long newRole) {
        roleRepository.updateRole(userId, newRole);
    }

    /**
     * Bloquea un usuario por su ID.
     *
     * @param id ID del usuario a bloquear.
     * @return true si el usuario se bloqueó correctamente, false si ocurrió un
     *         error.
     */
    @Override
    public boolean blockUser(Long id) {
        // Obtiene el UserAccess por el ID del usuario.
        UserAccess userAccess = userAccessService.findById(id);

        if (userAccess != null) {
            // Cambia el estado de bloqueo a true (Bloqueado).
            userAccessService.lock(userAccess);
            // Restablece los intentos fallidos.
            userAccessRepository.updateCounter(id, 2);

            // Guardar en la base de datos
            return true;
        } else {
            logWriter.writeError("Ha ocurrido un error en 'UserServiceImpl.java' durante el bloqueo del usuario con id "
                    + id + "ERROR: userAcces es NULL!");
            // userAccess es NULL! ");
            return false;
        }
    }

    /**
     * Desbloquea un usuario por su ID.
     *
     * @param id ID del usuario a desbloquear.
     * @return true si el usuario se desbloqueó correctamente, false si ocurrió un
     *         error.
     */
    @Override
    public boolean unBlockUser(Long id) {
        // Obtiene el UserAccess por el ID del usuario.
        UserAccess userAccess = userAccessService.findById(id);

        if (userAccess != null) {
            // Cambia el estado de bloqueo (invierte el estado actual).
            userAccessService.unLock(userAccess);
            // Restablece los intentos fallidos.
            userAccessService.resetFailedAttempts(userAccess);
            return true;
        } else {
            logWriter.writeError(
                    "Ha ocurrido un error en 'UserServiceImpl.java' durante el desbloqueo del usuario con id " + id
                            + "ERROR: userAccess es NULL!");
            return false;
        }
    }

    /**
     * Obtiene los roles de un usuario por su ID.
     *
     * @param userId El ID del usuario del que se quieren obtener los roles.
     * @return Una lista de roles asociados al usuario.
     */
    @Override
    public List<String> getRolesByUserId(Long userId) {
        List<Role> roles = userRoleRepository.findRolesByUserId(userId);
        return roles.stream().map(Role::getDisplayName).collect(Collectors.toList());
    }

    public String getUsernameById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user != null ? user.getUsername() : null;
    }

}
