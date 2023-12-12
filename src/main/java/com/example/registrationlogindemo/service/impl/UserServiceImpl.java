package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
import com.example.registrationlogindemo.functions.LogWriter;
import com.example.registrationlogindemo.repository.RoleRepository;
import com.example.registrationlogindemo.repository.UserAccessRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.service.UserAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserAccessRepository userAccessRepository;

    @Autowired
    private LogWriter logWriter;

    @Autowired
    private UserAccessService userAccessService;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, UserAccessRepository userAccessRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccessRepository = userAccessRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User fetchUser(String user) {
        return userRepository.fetchUser(user);
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        UserAccess userAccess = new UserAccess();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password once we integrate spring security
        // user.setPassword(userDto.getPassword());
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

    @Override
    public UserAccess findById(Integer id) {
        return userAccessRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(String individualRole) {
        Role role = new Role();
        role.setName(individualRole);
        return roleRepository.save(role);
    }

    @Override
    public void updateUsername(Long userId, String username) {
        userRepository.updateUsername(userId, username);
    }

    @Override
    public void updateEmail(Long userId, String email) {
        userRepository.updateEmail(userId, email);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        userRepository.updatePassword(userId, password);
    }

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
            // logWriter.writeLog("El usuario con id '" + id + "' ha sido bloqueado, por un
            // administrador.");
            return true;
        } else {
            logWriter.writeError("Ha ocurrido un error en 'UserServiceImpl.java' durante el bloqueo del usuario con id "
                    + id + "ERROR: userAcces es NULL!");
            // userAccess es NULL! ");
            return false;
        }
    }

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

}
