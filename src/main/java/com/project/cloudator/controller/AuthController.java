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
import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.functions.Regex;
import com.project.cloudator.service.UserInfoService;
import com.project.cloudator.service.UserService;

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

        User userDb = userService.fetchUser(user.getUsername());
        redirectAttributes.addAttribute("success", true);
        logwriter.writeLog("El usuario con id '" + userDb.getId() + "' ha sido creado");
        return "redirect:/register";
    }
}
