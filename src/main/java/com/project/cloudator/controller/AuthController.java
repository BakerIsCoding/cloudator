package com.project.cloudator.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.functions.Regex;
import com.project.cloudator.service.UserInfoService;
import com.project.cloudator.service.UserService;
import com.project.cloudator.service.SecurityService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogWriter logwriter;

    @Autowired
    private Regex regex;

    // private final CustomUserDetails customUserDetails;

    /**
     * Returns the index.
     * 
     * @return The name of the index view.
     * 
     *         @GetMapping("/")
     *         public String home() {
     *         return "index";
     *         }
     */

    /**
     * Returns the login form.
     * 
     * @return The name of the login form view.
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * @GetMapping("/login?error")
     * public String loginError() {
     * return "error";
     * }
     */

    /**
     * Returns the registration form.
     *
     * @param model The model object for the view.
     * @return The name of the registration form view.
     */
    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Handles the registration form submit request.
     *
     * @param user               The UserDto object containing the user data.
     * @param result             The BindingResult object for validation errors.
     * @param model              The model object for the view.
     * @param redirectAttributes The RedirectAttributes object for redirecting with
     *                           attributes.
     * @return The name of the view.
     */

    @Value("${secretencryptor}")
    private String SECRET_KEY_ENCRYPTOR;

    @PostMapping("/post/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        User mailExisting = userService.findByEmail(user.getEmail());
        User userExisting = userService.findByUsername(user.getUsername());

        if (mailExisting != null) {
            return "redirect:/register?errormail=1";
        }
        if (userExisting != null) {
            return "redirect:/register?errorusername=1";
        }
        if (!regex.isValidPassword(user.getPassword())) {
            return "redirect:/register?errorpassword=1";
        }
        if (!regex.isValidMail(user.getEmail())) {
            return "redirect:/register?errorvalidmail=1";
        }

        userService.saveUser(user);
        User idUserExisting = userService.findByUsername(user.getUsername());
        // Creamos instancia UserInfo con todo nulo
        UserInfo userInfo = new UserInfo();
        userInfo.setId(idUserExisting.getId());
        userInfo.setName(null);
        userInfo.setSurname(null);
        userInfo.setBirthday(null);
        userInfo.setAddress(null);
        userInfo.setFoto(null);

        userInfoService.save(userInfo);

        ////////////////////////////////////////

        SecurityService security = new SecurityService(SECRET_KEY_ENCRYPTOR);
        String fileManagerServiceUrl = "http://management-pants.gl.at.ply.gg:27118/upload/create-directory";
        Long userId = idUserExisting.getId();

        String encryptedUserId = security.encryptData(userId.toString());
        String encodedUserId = Base64.getUrlEncoder().encodeToString(encryptedUserId.getBytes(StandardCharsets.UTF_8));

        String requestUrl = fileManagerServiceUrl + "?userId=" + encodedUserId;
        try {
            // Realiza la llamada POST para crear las carpetas.
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl,
                    null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logwriter.writeLog("Directorios creados correctamente para el usuario con id '" +
                        userId + "'");
            } else {
                logwriter.writeLog(
                        "Error al crear directorios para el usuario con id '" + userId + "': " +
                                response.getBody());
            }
        } catch (Exception e) {
            logwriter.writeLog("Error al comunicarse con el servidor de archivos: " +
                    e.getMessage());
        }

        ////////////////////////////////////////

        User userDb = userService.fetchUser(user.getUsername());
        redirectAttributes.addAttribute("success", true);
        logwriter.writeLog("El usuario con id '" + userDb.getId() + "' ha sido creado");
        return "redirect:/register";
    }
}
