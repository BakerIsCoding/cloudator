package com.project.cloudator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserAccess;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.repository.UserRepository;
import com.project.cloudator.service.UserAccessService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccess userAccess;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private LogWriter logWriter;

    /**
     * Carga los detalles del usuario por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return los detalles del usuario
     * @throws UsernameNotFoundException si el usuario no se encuentra
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User dbUser = userRepository.fetchUser(username);

            if (dbUser != null) {
                userAccess = userAccessService.findById(dbUser.getId());
                if (!userAccess.isIsblocked()) {
                    return new org.springframework.security.core.userdetails.User(
                            username,
                            dbUser.getPassword(),
                            mapRolesToAuthorities(dbUser.getRoles()));
                } else {
                    logWriter.writeLog("El usuario con id '" + dbUser.getId()
                            + "' ha intentado iniciar sesión, pero la cuenta está bloqueada");
                    throw new BadCredentialsException("La cuenta está bloqueada, contacta con un administrador");
                }
            } else {
                throw new BadCredentialsException("Usuario y/o contraseña incorrectos");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    /**
     * Mapea los roles del usuario a las autoridades de Spring Security.
     *
     * @param roles los roles del usuario
     * @return las autoridades del usuario
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}