package com.project.cloudator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Obtiene una lista de usuarios registrados.
     * 
     * @param model Modelo para almacenar los usuarios.
     * @return La vista para mostrar la lista de usuarios registrados.
     */
    @GetMapping("/admin/listaUsuarios")
    public String adminPanel(Model model) {
        org.springframework.security.core.Authentication authentication2 = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication2.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);

        Integer page = 0;
        Integer size = 10;
        List<UserDto> users = userService.findFirstXUsers(page, size);

        // Mapa para almacenar el acceso de cada usuario
        Map<Long, UserAccess> userAccessMap = new HashMap<>();
        for (UserDto userDto : users) {
            UserAccess access = repo.fetchUserAccess(userDto.getId());
            userAccessMap.put(userDto.getId(), access);
        }

        Map<Long, List<String>> roleMap = new HashMap<>();
        for (UserDto userDto : users) {
            List<String> roles = userService.getRolesByUserId(userDto.getId());
            roleMap.put(userDto.getId(), roles);
        }

        model.addAttribute("users", users);
        model.addAttribute("userAccessMap", userAccessMap);
        model.addAttribute("roleMap", roleMap);

        return "/admin/admin";
    }

    @GetMapping("/admin/logs")
    public String logsPanel(Model model) {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            // Asumiendo que los archivos están en un directorio específico
            Path logDir = Paths.get("./"); // Ajusta esta ruta
            List<String> fileNames = Files.list(logDir)
                    .filter(path -> path.toString().endsWith(".log"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
            model.addAttribute("fileNames", fileNames);
        } catch (IOException e) {
            model.addAttribute("error", "Error listing log files: " + e.getMessage());
        }

        return "/admin/logs";
    }

    @GetMapping("/admin/panel/logs/content")
    public ResponseEntity<String> getLogFileContent(@RequestParam String file) {
        Path filePath = Paths.get("./", file); // Asegúrate de validar y sanear la entrada
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error reading file: " + e.getMessage());
        }
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

    @GetMapping("/admin/users/upgrade/{id}")
    public String upgrade(@PathVariable Long id) {
        Long newRole = 2L;
        userService.updateRole(id, newRole);
        logWriter.writeLog(
                "El usuario con id '" + id + "' ha sido ascendido a Administrador por un SuperAdministrador.");
        return "redirect:/admin";
    }

}
