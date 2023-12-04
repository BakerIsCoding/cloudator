package com.example.registrationlogindemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.entity.UserAccess;
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
    @Autowired
    private RoleRepository;
*/
    @Autowired
    private UserAccess userAccess;

    @Autowired
    private UserAccessService userAccessService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;

            String userName = String.valueOf(user.getUsername());
            User finalUser = userRepository.fetchUser(userName);

            if (finalUser != null) {
                Long userId = finalUser.getId();


                String redirectUrl = defaultRedirectUrl + String.valueOf(userId);
                response.sendRedirect(response.encodeRedirectURL(redirectUrl));


                UserAccess userAccess = userAccessService.findById(finalUser.getId());
                if (userAccess.getCounter() > 0) {
                    userAccessService.resetFailedAttempts(userAccess);
                }

            }
        }
    }
}
