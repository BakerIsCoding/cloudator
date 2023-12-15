package com.example.registrationlogindemo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.functions.LogWriter;
import com.example.registrationlogindemo.functions.Regex;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    /*
     * public String password = "";
     * 
     * public String getPassword() {
     * return password;
     * }
     */

    @Autowired
    private Regex regex;

    @Autowired
    private UserService userService;

    @Autowired
    private LogWriter logWriter;

    @GetMapping("/users/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        model.addAttribute("users", userService.getUserById(id));
        User user = userService.getUserById(id);
        System.out.println(user.getId());
        return "/crud/menucrud";
    }

    // Modificar Usuario
    @GetMapping("/users/edit/{id}")
    public String edit(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, @PathVariable Long id) {

        model.addAttribute("users", userService.getUserById(id));
        return "/crud/edituser";
    }

    // Borra usuario id
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        logWriter.writeLog("El usuario con id '" + id + "' ha eliminado su cuenta.");
        return "redirect:/logout";
    }

    @PostMapping("/users/save/{id}/username")
    public String saveUsername(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, @PathVariable Long id) {
        User usernameExisting = userService.findByUsername(user.getUsername());

        if (!regex.isValidUsername(user.getUsername())) {
            return "redirect:/users/edit/" + id + "?error01=1";
        }
        if (usernameExisting != null) {
            return "redirect:/users/edit/" + id + "?error0=1";
        }
        String username = user.getUsername();
        userService.updateUsername(id, username);
        return "redirect:/users/edit/" + id + "?success0=1";
    }

    @PostMapping("/users/save/{id}/mail")
    public String saveEmail(@Valid UserDto user,
            BindingResult result, RedirectAttributes redirectAttributes,
            @PathVariable Long id) {

        User mailExisting = userService.findByEmail(user.getEmail());

        if (!regex.isValidMail(user.getEmail())) {
            return "redirect:/users/edit/" + id + "?error1=1";
        }
        if (mailExisting != null) {
            return "redirect:/users/edit/" + id + "?error1=1";
        }
        String mail = user.getEmail();
        userService.updateEmail(id, mail);

        return "redirect:/users/edit/" + id + "?success1=1";
    }

    @PostMapping("/users/save/{id}/password")
    public String savePassword(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, @PathVariable Long id) {
        if (!regex.isValidPassword(user.getPassword())) {
            result.rejectValue("password", null, "La contraseña no cumple con los requisitos");
        }
        // ERROR 2
        String username = user.getPassword();
        userService.updatePassword(id, username);

        return "redirect:/users/edit/" + id;
    }

    /*
     * @PostMapping("/testpassword")
     * public void postMethodName(@RequestBody Map<String, String> requestBody) {
     * if (requestBody.containsKey("password")) {
     * String passwordFromRequest = requestBody.get("password");
     * // Realizar acciones con la contraseña, por ejemplo, asignarla a la variable
     * de
     * // instancia 'password'
     * this.password = passwordFromRequest;
     * }
     * }
     */

}
