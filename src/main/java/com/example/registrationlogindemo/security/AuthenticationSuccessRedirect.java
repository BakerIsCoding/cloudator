package com.example.registrationlogindemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.util.Collection;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
import com.example.registrationlogindemo.functions.LogWriter;
import com.example.registrationlogindemo.repository.RoleRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.UserAccessService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class AuthenticationSuccessRedirect implements AuthenticationSuccessHandler {

    private final String defaultRedirectUrl = "/users/edit/";

    @Autowired
    private UserRepository userRepository;
    /*
     * @Autowired
     * private RoleRepository;
     */
    @Autowired
    private UserAccess userAccess;

    private LogWriter logWriter = new LogWriter();

    @Autowired
    private UserAccessService userAccessService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;

            String userName = user.getUsername();
            User finalUser = userRepository.fetchUser(userName);

            if (finalUser != null) {
                // Obtengo el rol del usuario
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                if (authorities != null && !authorities.isEmpty()) {
                    for (GrantedAuthority authority : authorities) {
                        // Obtengo el rol del usuario
                        String role = authority.getAuthority();
                        Long userId = finalUser.getId();
                        logWriter.writeLog("El usuario con id '" + userId +"' ha iniciado sesión, tiene el rol '" + role + "'");

                        if (role.equals("ROLE_USER") || role.equals("ROLE_PREMIUM")) {
                            // Pagina normal para el usuario
                            
                            String redirectUrl = defaultRedirectUrl + userId;
                            response.sendRedirect(response.encodeRedirectURL(redirectUrl));
                        } else if (role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPERADMIN")) {
                            String redirectUrl = "/admin/users";
                            response.sendRedirect(response.encodeRedirectURL(redirectUrl));
                        } else {
                            logWriter.writeError("No se que rol es " + role);
                            response.sendRedirect(response.encodeRedirectURL("/error"));
                        }
                    }
                }

                UserAccess userAccess = userAccessService.findById(finalUser.getId());
                if (userAccess.getCounter() > 0) {
                    logWriter.writeLog("El usuario con id '" + userAccess.getId() + "' ha iniciado sesión, he reiniciado a '0' los intentos fallidos");
                    userAccessService.resetFailedAttempts(userAccess);
                }
            }
        }
    }
}
