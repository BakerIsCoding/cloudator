package com.example.registrationlogindemo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.security.CustomUserDetailsService;
import com.example.registrationlogindemo.service.UserService;

@Controller
public class AdminController {

    private UserService userService;

    private final CustomUserDetailsService customUserDetailsService;

    // private final CustomUserDetails customUserDetails;

    public AdminController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/admin/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "/crud/menucrud";
        //return "users";
    }
}
