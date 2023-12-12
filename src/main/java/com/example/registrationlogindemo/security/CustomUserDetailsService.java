package com.example.registrationlogindemo.security;

import com.example.registrationlogindemo.controller.UserController;
import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
import com.example.registrationlogindemo.functions.LogWriter;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserAccessService;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private UserAccess userAccess;
    private UserAccessService userAccessService;
    private LogWriter logWriter;

    public CustomUserDetailsService(UserRepository userRepository, UserAccess userAccess,
            UserAccessService userAccessService, LogWriter logWriter) {
        this.userRepository = userRepository;
        this.userAccess = userAccess;
        this.userAccessService = userAccessService;
        this.logWriter = logWriter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            User dbUser = userRepository.fetchUser(username);

            if (dbUser != null) {
                userAccess = userAccessService.findById(dbUser.getId());
                if (!userAccess.isIsblocked()) {
                    return new org.springframework.security.core.userdetails.User(
                            dbUser.getUsername(),
                            dbUser.getPassword(),
                            mapRolesToAuthorities(dbUser.getRoles()));
                } else {
                    logWriter.writeLog("El usuario con id '" + dbUser.getId() + "' ha intentado iniciar sesión, pero la cuenta está bloqueada");
                    throw new BadCredentialsException("La cuenta está bloqueada, contacta con un administrador");
                }
            } else {
                throw new BadCredentialsException("Usuario y/o contraseña incorrectos");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}