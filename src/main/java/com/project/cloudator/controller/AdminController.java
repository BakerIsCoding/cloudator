package com.project.cloudator.controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.context.SecurityContextHolder;


import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.service.UserService;

@Controller
public class AdminController {

    // private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private LogWriter logWriter;

    @Autowired
    private UserService userService;

    /**
     * Obtiene una lista de usuarios registrados.
     * 
     * @param model Modelo para almacenar los usuarios.
     * @return La vista para mostrar la lista de usuarios registrados.
     */
    @GetMapping("/admin/")
    public String indexAdmin() {
        return "/index";
    }

    @GetMapping("/admin/panel")
    public String adminPanel(Model model) {
        org.springframework.security.core.Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication2.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);
        /*
         * Integer page = 0;
         * Integer size = 10;
         * List<UserDto> users = userService.findFirstXUsers(page, size);
         * 
         * model.addAttribute("users", users);
         */
        return "/admin/admin";
    }

    /**
     * Borra un usuario por su identificador.
     * 
     * @param id Identificador del usuario a borrar.
     * @return La vista para redirigir a la lista de usuarios.
     */
    @GetMapping("/admin/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        logWriter.writeLog("El usuario con id '" + id + "' ha sido eliminado por un administrador.");
        return "redirect:/admin";
    }

    /**
     * Bloquea un usuario por su identificador.
     * 
     * @param id Identificador del usuario a bloquear.
     * @return La vista para redirigir a la lista de usuarios.
     */
    @GetMapping("/admin/users/block/{id}")
    public String block(@PathVariable Long id) {
        Boolean isBlocked = userService.blockUser(id);
        if (isBlocked) {
            logWriter.writeLog("El usuario con id '" + id + "' ha sido bloqueado por un administrador.");
        }
        return "redirect:/admin";
    }

    /**
     * Desbloquea un usuario por su identificador.
     * 
     * @param id Identificador del usuario a desbloquear.
     * @return La vista para redirigir a la lista de usuarios.
     */
    @GetMapping("/admin/users/unblock/{id}")
    public String unblock(@PathVariable Long id) {
        Boolean isUnblocked = userService.unBlockUser(id);
        if (isUnblocked) {
            logWriter.writeLog("El usuario con id '" + id + "' ha sido desbloqueado por un administrador.");
        }
        return "redirect:/admin";
    }
}
