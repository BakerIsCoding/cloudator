package com.project.cloudator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.functions.Regex;
import com.project.cloudator.service.UserInfoService;
import com.project.cloudator.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    private Regex regex;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogWriter logWriter;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/users/{id}")
    public String home(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, @PathVariable Long id) {

        model.addAttribute("users", userService.getUserById(id));
        return "index";
    }

    /**
     * Elimina un usuario específico.
     *
     * @param id El ID del usuario a eliminar.
     * @return La vista para redirigir después de eliminar el usuario.
     */
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        logWriter.writeLog("El usuario con id '" + id + "' ha eliminado su cuenta.");
        return "redirect:/delete";
    }

    /**
     * Guarda el nombre de usuario actualizado de un usuario.
     *
     * @param user   El objeto UserDto con los datos del usuario.
     * @param result El objeto BindingResult para manejar los errores de validación.
     * @param model  El modelo para almacenar los datos del usuario.
     * @param id     El ID del usuario.
     * @return La vista para redirigir después de guardar el nombre de usuario.
     */
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

    /**
     * Guarda el correo electrónico actualizado de un usuario.
     *
     * @param user   El objeto UserDto con los datos del usuario.
     * @param result El objeto BindingResult para manejar los errores de validación.
     * @param id     El ID del usuario.
     * @return La vista para redirigir después de guardar el correo electrónico.
     */
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

    /**
     * Guarda la contraseña actualizada de un usuario.
     *
     * @param user   El objeto UserDto con los datos del usuario.
     * @param result El objeto BindingResult para manejar los errores de validación.
     * @param model  El modelo para almacenar los datos del usuario.
     * @param id     El ID del usuario.
     * @return La vista para redirigir después de guardar la contraseña.
     */
    @PostMapping("/users/save/{id}/password")
    public String savePassword(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, @PathVariable Long id) {
        if (!regex.isValidPassword(user.getPassword())) {
            return "redirect:/users/edit/" + id + "?error2=1";
        }

        String password = user.getPassword();
        userService.updatePassword(id, password);

        return "redirect:/users/edit/" + id + "?success2=1";
    }

    @GetMapping("/status")
    public String status() {
        return "status";
    }

    /**
     * Muestra los detalles de un usuario específico.
     *
     * @param id    El ID del usuario.
     * @param model El modelo para almacenar los datos del usuario.
     * @return La vista para mostrar los detalles del usuario.
     */
    @GetMapping("/users/edit/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        // Se añade el objeto userInfo a thymeleaf
        UserInfo userinfo = userInfoService.getUserInfoById(id);
        model.addAttribute("userinfo", userinfo);

        return "/settings";
    }

    @GetMapping("/users/files/{id}")
    public String showFiles(@PathVariable Long id, Model model) {
        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        return "/files";
    }

    @PostMapping("/settingsuser/post")
    public String postUserInfo(@Valid @ModelAttribute("user") User userr,
            BindingResult result) {

        Long userId = userr.getId();
        String userIdString = userId.toString();

        try {
            if (!regex.isValidPassword(userr.getPassword())) {
                return "redirect:/users/edit/" + userIdString + "?error2=1";
            }
            userService.updateUsername(userId, userr.getUsername());
            userService.updateEmail(userId, userr.getEmail());
            userService.updatePassword(userId, userr.getPassword());

            return "redirect:/users/edit/" + userIdString + "?success=1";
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println(e.getMessage());
            return "redirect:/users/edit/" + userIdString + "?error=1";
        }

    }

    @PostMapping("/settingsoptional/post")
    public String postOptional(@Valid @ModelAttribute("userinfo") UserInfo userinfo,
            BindingResult result) {

        String userId = userinfo.getId().toString();
        try {
            userInfoService.save(userinfo);
            return "redirect:/users/edit/" + userId + "?success=1";
        } catch (Exception e) {
            return "redirect:/users/edit/" + userId + "?error=1";
        }

    }

}
