package com.example.registrationlogindemo.controller;

import java.util.regex.Pattern;
import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.security.CustomUserDetails;
import com.example.registrationlogindemo.security.CustomUserDetailsService;
import com.example.registrationlogindemo.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    private final CustomUserDetailsService customUserDetailsService;

    //private final CustomUserDetails customUserDetails;

    public AuthController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/loginpost")
    public String checkLogin(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userData", user);
        return "redirect:/loginpostfinal";

        
    }

    /*
     * @PostMapping("/loginpost")
     * public String checkLogin(@Valid @ModelAttribute("user") UserDto user,
     * BindingResult result,
     * Model model,
     * RedirectAttributes redirectAttributes) {
     * System.out.println(user.getUsername());
     * System.out.println(user.getPassword());
     * 
     * 
     * System.out.println("hola jefe");
     * UserDetails userDetails =
     * customUserDetailsService.loadUserByUsernameAndPass(user.getUsername(),
     * user.getPassword());
     * // UserDetails userDetailsa =
     * // customUserDetailsService.loadUserByUsername(user.getUsername());
     * 
     * // CustomUserDetailsService cuds = CustomUserDetailsService();
     * 
     * return "fino";
     * }
     */
    /*
     * @PostMapping("/loginp")
     * public String loginPost() {
     * return "users";
     * }
     */
    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
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

        if (!isValidPassword(user.getPassword())) {
            result.rejectValue("password", null, "La contraseña no cumple con los requisitos");
        }
        if (!isValidMail(user.getEmail())) {
            result.rejectValue("email", null, "Este no es un mail valido");
        }

        if (result.hasErrors()) {
            // Manejo de errores, mostrando mensajes específicos en el formulario.
            return "register";
        }

        userService.saveUser(user);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/register";
    }

    private boolean isValidPassword(String password) {

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$;._\\-*]).{8,64}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isValidMail(String mail) {

        String mailPattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return Pattern.matches(mailPattern, mail);
    }

    @GetMapping("/admin/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    /*
     * @RequestMapping("/error")
     * public String handleError(HttpServletRequest request) {
     * Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
     * 
     * if (status != null) {
     * Integer statusCode = Integer.valueOf(status.toString());
     * 
     * if (statusCode == HttpStatus.NOT_FOUND.value()) {
     * return "error-404";
     * 
     * } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
     * return "error-500";
     * }
     * }
     * return "error";
     * }
     */
}
