package com.example.registrationlogindemo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    /* 
    public String password = "";

    public String getPassword() {
        return password;
    }
    */
    
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        model.addAttribute("users", userService.getUserById(id));
        return "/crud/menucrud";
    }

    // Modificar Usuario
    @GetMapping("users/edit/{id}")
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
        return "redirect:/users";
    }

    /* 
    @PostMapping("/testpassword")
    public void postMethodName(@RequestBody Map<String, String> requestBody) {
        if (requestBody.containsKey("password")) {
            String passwordFromRequest = requestBody.get("password");
            // Realizar acciones con la contraseña, por ejemplo, asignarla a la variable de
            // instancia 'password'
            this.password = passwordFromRequest;
        }
    }
    */

}
