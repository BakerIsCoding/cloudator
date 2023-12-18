package com.project.cloudator.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserAccess;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.service.UserAccessService;
import com.project.cloudator.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private CustomUserDetails customUserDetails;

    @Autowired
    private LogWriter logWriter;

    /**
     * Maneja el fallo en la autenticación de inicio de sesión.
     * 
     * @param request   La solicitud HTTP.
     * @param response  La respuesta HTTP.
     * @param exception La excepción de autenticación.
     * @throws IOException      Si ocurre un error de entrada/salida.
     * @throws ServletException Si ocurre un error en el servlet.
     * @return void
     */
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
                        Integer counterUpdated = userAccess.getCounter() + 1;
                        exception = new LockedException(
                                "Usuario y/o contraseña incorrecta.");
                        userAccessService.increaseFailedAttempts(userAccess);
                        logWriter.writeLog("El usuario con id '" + userAccess.getId()
                                + "' ha intentado iniciar sesión, tiene '" + counterUpdated
                                + "' intentos fallidos");
                    } else {
                        userAccessService.lock(userAccess);
                        exception = new LockedException(
                                "Se ha superado el maximo de intentos y la cuenta ha sido bloqueada, contacta con un administrador.");
                        logWriter.writeLog("El usuario con id '" + userAccess.getId() + "' ha sido bloqueado");
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
