package com.project.cloudator.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.project.cloudator.service.UserService;

@Component
public class UserImg {

    @Autowired
    private UserService userService;

    public Long getUserId() {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);
        return userServerId;
    }

}