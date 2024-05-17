package com.project.cloudator.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.project.cloudator.service.UserService;

@Component
public class UserImg {

    @Autowired
    private UserService userService;

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return El ID del usuario autenticado, o null si no hay ningún usuario
     *         autenticado.
     */
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            return userService.getUserIdByUsername(username);
        }
        return null;
    }
}