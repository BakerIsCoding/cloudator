package com.example.registrationlogindemo.test;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.registrationlogindemo.service.UserService;

public class CustomUserDetailsService implements UserDetailsService {

    // Aquí deberías tener la lógica para cargar los detalles del usuario desde tu
    // fuente de datos (por ejemplo, base de datos)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserService userService;

        // Implementación para cargar los detalles del usuario basándose en el nombre de
        // usuario
        // Aquí deberías recuperar los detalles del usuario, incluida su contraseña,
        // desde tu fuente de datos
        // (por ejemplo, una base de datos)

        // Supongamos que obtienes estos detalles de una base de datos o algún otro
        // almacenamiento
        //String storedUsername = userService;
        String storedPassword = "$2a$10$uxUvd/4Rt/h8C/Y91o9mrOmHfWzrYR/2x5KLk44WbOSbK/C0cQyi6"; // Contraseña hasheada
                                                                                                // (ejemplo)

        // Devuelve un objeto UserDetails (o una implementación personalizada) con los
        // detalles del usuario
        return org.springframework.security.core.userdetails.User.builder()
                .username("xd")
                .password(storedPassword)
                .roles("USER")
                .build();
    }
}
