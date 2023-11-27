package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.RoleRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserService;

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

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password once we integrate spring security
        // user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        //Definimos los roles
        String roleName = "ROLE_SUPERADMIN";
        String role1Name = "ROLE_ADMIN";
        String role2Name = "ROLE_PREMIUM";
        String role3Name = "ROLE_USER";

        //Asignamos la respuesta de la base de datos an una variable tipo Role
        Role role = roleRepository.findByName(roleName);
        Role role1 = roleRepository.findByName(role1Name);
        Role role2 = roleRepository.findByName(role2Name);
        Role role3 = roleRepository.findByName(role3Name);

        
        //Comprovamos que existan los roles
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

        //Asignamos al usuario el rol predeterminado, en nuestro caso se encuentra en la variable roleName
        user.setRoles(Arrays.asList(role3));

        //Guardamos el usuario final en la base de datos
        userRepository.save(user);

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
}
