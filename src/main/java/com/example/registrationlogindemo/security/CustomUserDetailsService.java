package com.example.registrationlogindemo.security;

import com.example.registrationlogindemo.controller.UserController;
import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.UserRepository;

import org.springframework.context.annotation.Bean;
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
    private final PasswordEncoder passwordEncoder; // Cambiado a la interfaz PasswordEncoder

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Utiliza la interfaz PasswordEncoder
    }

    @Bean
    public BCryptPasswordEncoder custompasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*
     * public UserDetails loadUserByUsernameAndPass(String username, String
     * password) throws UsernameNotFoundException {
     * User dbUser = userRepository.fetchUser(username);
     * String providedRawPassword = "aB123jndsakj$";
     * 
     * if (dbUser != null) {
     * // Codifica la contraseña proporcionada antes de compararla
     * 
     * if (passwordEncoder.matches(password, dbUser.getPassword())) {
     * System.out.println("entro2");
     * return new org.springframework.security.core.userdetails.User(
     * dbUser.getUsername(),
     * dbUser.getPassword(),
     * mapRolesToAuthorities(dbUser.getRoles()));
     * } else {
     * System.out.println("entro1");
     * throw new UsernameNotFoundException("Contraseña incorrecta");
     * }
     * } else {
     * System.out.println("Entro");
     * throw new UsernameNotFoundException("Usuario no encontrado");
     * 
     * }
     * }
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = userRepository.fetchUser(username);
        String providedRawPassword = "aB123jndsakj$";

        UserController userController = new UserController();
        //String htmlPass = userController.getPassword();

        if (dbUser != null) {
            // Codifica la contraseña proporcionada antes de compararla

            if (passwordEncoder.matches(providedRawPassword, dbUser.getPassword())) {
                System.out.println("entro2");
                return new org.springframework.security.core.userdetails.User(
                        dbUser.getUsername(),
                        dbUser.getPassword(),
                        mapRolesToAuthorities(dbUser.getRoles()));
            } else {
                System.out.println("entro1");
                throw new UsernameNotFoundException("Contraseña incorrecta");
            }
        } else {
            System.out.println("Entro");
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