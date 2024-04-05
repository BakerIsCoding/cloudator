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
import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.User;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.service.UserService;
import com.project.cloudator.repository.RoleRepository;
import com.project.cloudator.repository.UserAccessRepository;
import com.project.cloudator.repository.UserRepository;
import com.project.cloudator.service.UserAccessService;
import com.project.cloudator.service.UserInfoService;
import com.project.cloudator.entity.UserAccess;
import com.project.cloudator.entity.UserInfo;

@Controller
public class AdminController {

    // private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogWriter logWriter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccessRepository repo;

    @Autowired
    private RoleRepository RoleService;

    /**
     * Obtiene una lista de usuarios registrados.
     * 
     * @param model Modelo para almacenar los usuarios.
     * @return La vista para mostrar la lista de usuarios registrados.
     */
    @GetMapping("/admin/panel")
    public String adminPanel(Model model) {
        org.springframework.security.core.Authentication authentication2 = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication2.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);

        UserAccess userAccess = repo.fetchUserAccess(userServerId);

        Integer page = 0;
        Integer size = 10;
        List<UserDto> users = userService.findFirstXUsers(page, size);

        model.addAttribute("users", users);
        model.addAttribute("userAccess", userAccess);

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

    @GetMapping("/admin/edit/{id}")
    public String showUser(Model model, @PathVariable Long id) {

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        // Se añade el objeto userInfo a thymeleaf
        UserInfo userinfo = userInfoService.getUserInfoById(id);
        model.addAttribute("userinfo", userinfo);

        // Se añade el objeto userInfo a thymeleaf
        Role role = RoleService.findByName("ROLE_ADMIN");
        model.addAttribute("role", role);

        return "/settings";
    }

}
