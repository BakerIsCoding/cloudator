package com.example.registrationlogindemo.test;

import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserService;

public class CustomUserDetailsServiceTest implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Implementación para cargar los detalles del usuario basándose en el nombre de
        // usuario
        // Aquí deberías recuperar los detalles del usuario, incluida su contraseña,
        // desde tu fuente de datos
        // (por ejemplo, una base de datos)

        // Supongamos que obtienes estos detalles de una base de datos o algún otro
        // almacenamiento
        User dbUser = (User) userRepository.fetchUser(username);

        String storedUsername = dbUser.getUsername();
        String storedPassword = dbUser.getPassword();
        System.err.println("Entro en Custom");

        // Devuelve un objeto UserDetails (o una implementación personalizada) con los
        // detalles del usuario
        return org.springframework.security.core.userdetails.User.builder()
                .username(storedUsername)
                .password(storedPassword)
                // .roles("USER")
                .build();
    }
}
