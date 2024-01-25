package com.project.cloudator.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.functions.Regex;
import com.project.cloudator.service.UserService;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private LogWriter logwriter;

    @Autowired
    private Regex regex;
    // private final CustomUserDetails customUserDetails;

    /**
     * Returns the index.
     * 
     * @return The name of the index view.
     */
    @GetMapping("index")
    public String home() {
        return "index";
    }

    /**
     * Returns the login form.
     * 
     * @return The name of the login form view.
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

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

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        User mailExisting = userService.findByEmail(user.getEmail());
        User userExisting = userService.findByUsername(user.getUsername());

        if (mailExisting != null) {
            result.rejectValue("email", null, "Ya existe un usuario con este email");
        }

        if (userExisting != null) {
            result.rejectValue("username", null, "Ya existe un usuario con este nombre");
        }

        if (!regex.isValidPassword(user.getPassword())) {
            result.rejectValue("password", null, "La contraseña no cumple con los requisitos");
        }
        if (!regex.isValidMail(user.getEmail())) {
            result.rejectValue("email", null, "Este no es un mail valido");
        }

        if (result.hasErrors()) {
            // Manejo de errores, mostrando mensajes específicos en el formulario.
            return "register";
        }
        userService.saveUser(user);
        User userDb = userService.fetchUser(user.getUsername());
        redirectAttributes.addAttribute("success", true);
        logwriter.writeLog("El usuario con id '" + userDb.getId() + "' ha sido creado");
        return "redirect:/register";
    }
}
