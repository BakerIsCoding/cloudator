package com.example.registrationlogindemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.functions.LogWriter;
import com.example.registrationlogindemo.security.CustomUserDetailsService;
import com.example.registrationlogindemo.service.UserService;

@Controller
public class AdminController {

    private UserService userService;

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private final LogWriter logWriter = new LogWriter();

    // private final CustomUserDetails customUserDetails;

    public AdminController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/admin/users")
    public String listRegisteredUsers(Model model) {
        Integer page = 0;
        Integer size = 10;
        List<UserDto> users = userService.findFirstXUsers(page, size);

        model.addAttribute("users", users);
        return "/crud/menucrud";
    }

    // Borra usuario id
    @GetMapping("/admin/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        logWriter.writeLog("El usuario con id '" + id + "' ha sido eliminado por un administrador.");
        return "redirect:/admin/users";
    }

    // Desbloquear y bloquear usuario por id

    @GetMapping("/admin/users/block/{id}")
    public String block(@PathVariable Long id) {
        Boolean isBlocked = userService.blockUser(id);
        if (isBlocked) {
            logWriter.writeLog("El usuario con id '" + id + "' ha sido bloqueado por un administrador.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/unblock/{id}")
    public String unblock(@PathVariable Long id) {
        Boolean isUnblocked = userService.unBlockUser(id);
        if (isUnblocked) {
            logWriter.writeLog("El usuario con id '" + id + "' ha sido desbloqueado por un administrador.");
        }
        return "redirect:/admin/users";
    }
}
