package com.example.registrationlogindemo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
import com.example.registrationlogindemo.repository.UserAccessRepository;
import com.example.registrationlogindemo.service.UserAccessService;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserService userService;
    private UserAccessService userAccessService;
    private CustomUserDetails customUserDetails;
    private UserAccessRepository userAccessRepository;

    public CustomLoginFailureHandler(UserAccessService userAccessService, CustomUserDetails customUserDetails) {
        this.userAccessService = userAccessService;
        this.customUserDetails = customUserDetails;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        try {
            String username = request.getParameter("username");
            User user = userService.findByUsername(username);
            UserAccess userAccess = userAccessService.findById(user.getId());

            if (userAccess != null) {
                if (customUserDetails.isAccountNonLocked()) {
                    if (userAccess.getCounter() < UserAccessService.MAX_FAILED_ATTEMPTS - 1) {
                        exception = new LockedException(
                                "Usuario y/o contraseña incorrecta.");
                        userAccessService.increaseFailedAttempts(userAccess);
                    } else {
                        userAccessService.lock(userAccess);
                        exception = new LockedException(
                                "Se ha superado el maximo de intentos y la cuenta ha sido bloqueada, contacta con un administrador.");
                    }
                }

            }
            super.setDefaultFailureUrl("/login?error");
            super.onAuthenticationFailure(request, response, exception);
        } catch (Exception e) {
            exception = new LockedException(
                    "Usuario y/o contraseña incorrecta.");
            
            super.setDefaultFailureUrl("/login?error");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
