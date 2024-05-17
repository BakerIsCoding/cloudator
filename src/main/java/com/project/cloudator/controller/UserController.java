package com.project.cloudator.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//////

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.http.HttpHeaders;
import java.nio.file.StandardOpenOption;

//////

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

//////

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.File;
import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.functions.Regex;
import com.project.cloudator.functions.UserImg;
import com.project.cloudator.repository.RoleRepository;
import com.project.cloudator.service.UserInfoService;
import com.project.cloudator.service.UserService;
import com.project.cloudator.repository.RoleRepository;
import com.project.cloudator.repository.UserAccessRepository;
import com.project.cloudator.repository.UserInfoRepository;
import com.project.cloudator.repository.UserRoleRepository;
import com.project.cloudator.service.FileService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class UserController {

    @Autowired
    private Regex regex;

    @Autowired
    private RoleRepository RoleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogWriter logWriter;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserImg userMethods;

    @Value("${domain}")
    private String domain;

    @GetMapping("/")
    public String home() {
        return "landing";
    }

    /**
     * Maneja la solicitud GET para mostrar la página principal del usuario.
     *
     * @param user   El objeto UserDto que se validará.
     * @param result El objeto BindingResult que contendrá los resultados de la
     *               validación.
     * @param model  El objeto Model que se utilizará para pasar atributos a la
     *               vista.
     * @return El nombre de la vista de la página principal del usuario.
     */
    @GetMapping("/users/")
    public String home(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model) {

        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        model.addAttribute("users", userService.getUserById(userServerId));

        List<Role> roles = userRoleRepository.findRolesByUserId(userServerId);
        String userRole = "";

        Long totalStorageUsedLong = fileService.getStorage(userServerId);
        BigInteger totalStorageUsed = BigInteger.valueOf(totalStorageUsedLong);
        BigInteger maxStorage = BigInteger.ZERO;

        if (!roles.isEmpty()) {
            Role firstRole = roles.get(0);
            userRole = firstRole.getName();
            maxStorage = firstRole.getMaxStorage();
        } else {
            System.out.println("Error, el Rol está vacío.");
        }

        /*
         * switch (userRole) {
         * case "ROLE_USER":
         * maxStorage = new BigInteger("10737418240");
         * break;
         * case "ROLE_PREMIUM":
         * maxStorage = new BigInteger("107374182400");
         * break;
         * case "ROLE_ADMIN":
         * maxStorage = new BigInteger("1073741824000");
         * break;
         * case "ROLE_SUPERADMIN":
         * maxStorage = new BigInteger("1073741824000");
         * break;
         * 
         * default:
         * break;
         * }
         */

        BigInteger remainingStorage = maxStorage.subtract(totalStorageUsed);

        Float totalStorageUsedInteger = formatBytesAsFloatGB(totalStorageUsed);
        Float remainingStorageInteger = formatBytesAsFloatGB(remainingStorage);

        model.addAttribute("espacioOcupado", totalStorageUsedInteger);
        model.addAttribute("espacioLibre", remainingStorageInteger);

        model.addAttribute("fechasNormal", fileService.countFilesFromLastWeekGrouped());
        model.addAttribute("fechasUser", fileService.countFilesForOwnerFromLastWeekGrouped(userServerId));

        model.addAttribute("totalStorageUsed", formatBytes(totalStorageUsed));
        model.addAttribute("remainingStorage", formatBytes(remainingStorage));
        model.addAttribute("maxStorage", formatBytes(maxStorage));

        return "index";
    }

    public String formatBytes(BigInteger bytes) {
        if (bytes.equals(BigInteger.ZERO))
            return "0 Bytes";
        BigInteger KILOBYTE = BigInteger.valueOf(1024);
        BigInteger MEGABYTE = KILOBYTE.multiply(KILOBYTE);
        BigInteger GIGABYTE = MEGABYTE.multiply(KILOBYTE);
        BigInteger TERABYTE = GIGABYTE.multiply(KILOBYTE);

        double value;
        String unit;
        if (bytes.compareTo(TERABYTE) >= 0) {
            value = bytes.divide(TERABYTE).doubleValue() + (bytes.mod(TERABYTE).doubleValue() / TERABYTE.doubleValue());
            unit = " TB";
        } else if (bytes.compareTo(GIGABYTE) >= 0) {
            value = bytes.divide(GIGABYTE).doubleValue() + (bytes.mod(GIGABYTE).doubleValue() / GIGABYTE.doubleValue());
            unit = " GB";
        } else if (bytes.compareTo(MEGABYTE) >= 0) {
            value = bytes.divide(MEGABYTE).doubleValue() + (bytes.mod(MEGABYTE).doubleValue() / MEGABYTE.doubleValue());
            unit = " MB";
        } else if (bytes.compareTo(KILOBYTE) >= 0) {
            value = bytes.divide(KILOBYTE).doubleValue() + (bytes.mod(KILOBYTE).doubleValue() / KILOBYTE.doubleValue());
            unit = " KB";
        } else {
            value = bytes.doubleValue();
            unit = " Bytes";
        }
        return String.format("%.2f", value) + unit;
    }

    public Float formatBytesAsFloatGB(BigInteger bytes) {
        BigInteger KILOBYTE = BigInteger.valueOf(1024);
        BigInteger MEGABYTE = KILOBYTE.multiply(KILOBYTE);
        BigInteger GIGABYTE = MEGABYTE.multiply(KILOBYTE);

        // Calcula los gigabytes como un valor float
        float gigabytes = bytes.divide(GIGABYTE).floatValue()
                + (bytes.mod(GIGABYTE).floatValue() / GIGABYTE.floatValue());
        return gigabytes;
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
        return "redirect:/logout";
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
        logWriter.writeLog(
                "El usuario con id '" + id + "' ha cambiado su nombre de usuario a '" + user.getUsername() + "'.");

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
        logWriter.writeLog(
                "El usuario con id '" + id + "' ha cambiado su correo electrónico a '" + user.getEmail() + "'.");

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

        logWriter.writeLog("El usuario con id '" + id + "' ha cambiado su contraseña.");

        return "redirect:/users/edit/" + id + "?success2=1";
    }

    /**
     * Muestra los detalles de un usuario específico.
     *
     * @param id    El ID del usuario.
     * @param model El modelo para almacenar los datos del usuario.
     * @return La vista para mostrar los detalles del usuario.
     */
    @GetMapping("/users/edit/")
    public String showUser(Model model, Authentication authentication) {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);

        // Se añade el objeto userInfo a thymeleaf
        UserInfo userinfo = userInfoService.getUserInfoById(userServerId);
        model.addAttribute("userinfo", userinfo);

        return "/settings";
    }

    /**
     * Maneja la solicitud GET para mostrar los archivos del usuario.
     *
     * @param model El objeto Model que se utilizará para pasar atributos a la
     *              vista.
     * @return El nombre de la vista de la página de archivos del usuario.
     */
    @GetMapping("/users/files/")
    public String showFiles(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        List<File> files = fileService.findFilesByOwner(userServerId);

        files.forEach(file -> {
            file.setFiletype(mimeToString(file.getFiletype()));
        });

        model.addAttribute("files", files);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);
        // model.addAttribute("userIMG",
        // domain + "/upload/" + userServerId + "/pfp/profile.jpg");

        return "/files";
    }

    // QUIZAS ERROR DE ESTO

    public String mimeToString(String mimeType) {
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType type = allTypes.forName(mimeType);
            String extension = type.getExtension();
            if (mimeType == "application/x-msdownload") {
                extension = ".exe";
            }
            // delete "."
            extension = extension.substring(1);
            return extension;
        } catch (Exception e) {
            System.out.println("Error al determinar el tipo de archivo, " +
                    e.getMessage());
            return "Otro";
        }
    }

    /**
     * Maneja la solicitud POST para actualizar la imagen de perfil del usuario.
     *
     * @param user               El objeto User que contiene la información del
     *                           usuario.
     * @param image              El archivo MultipartFile que contiene la nueva
     *                           imagen de perfil.
     * @param redirectAttributes Los atributos de redirección que se utilizarán para
     *                           enviar mensajes flash.
     * @return Una redirección a la página de edición del usuario.
     * @throws IllegalStateException Si ocurre un error al manejar el archivo de
     *                               imagen.
     * @throws IOException           Si ocurre un error de entrada/salida.
     * @throws InterruptedException  Si la operación es interrumpida.
     */
    @PostMapping("/post/settingsimage")
    public String postUserIname(@ModelAttribute("user") User user,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) throws IllegalStateException, IOException, InterruptedException {

        Long authUserId = userMethods.getUserId();

        /////
        Path tempFile = Paths.get(System.getProperty("java.io.tmpdir"), image.getOriginalFilename());
        image.transferTo(tempFile);

        // Prepare the URI and the HttpRequest with multipart/form-data
        URI uri = URI.create(domain + "upload/" + authUserId + "/pfpic");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/octet-stream")
                .POST(BodyPublishers.ofFile(tempFile))
                .build();

        // Send the request using HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println("URL foto perfil: " + uri);

        /////

        String url = domain + "upload/" + authUserId + "/pfp/profile.jpg";

        UserInfo userinfo = new UserInfo();
        userinfo.setId(authUserId);
        userinfo.setFoto(url);
        System.out.println("Url subida: " + userinfo.getFoto());
        userInfoRepository.updatePcp(userinfo.getId(), userinfo.getFoto());

        redirectAttributes.addFlashAttribute("message", "Imagen cargada con éxito!");
        logWriter.writeLog("El usuario con id '" + authUserId + "' ha cambiado su imagen de perfil.");
        return "redirect:/users/edit/";
    }

    /**
     * Maneja la solicitud POST para actualizar la información del usuario(Nombre de
     * Usuario, Mail y Contraseña).
     *
     * @param userr  El objeto User que contiene la información del usuario.
     * @param result El objeto BindingResult que contendrá los resultados de la
     *               validación.
     * @param model  El objeto Model que se utilizará para pasar atributos a la
     *               vista.
     * @return Una redirección a la página de edición del usuario con un mensaje de
     *         éxito o error.
     */
    @PostMapping("/post/settingsuser")
    public String postUserInfo(@Valid @ModelAttribute("user") User userr,
            BindingResult result, Model model) {

        Long userId = userr.getId();
        // String userIdString = userId.toString();
        Role role = RoleRepository.fetchRoleById(userId);
        model.addAttribute("role", role);

        try {
            if (!regex.isValidPassword(userr.getPassword())) {
                return "redirect:/users/edit/?error2=1";
            }
            userService.updateUsername(userId, userr.getUsername());
            userService.updateEmail(userId, userr.getEmail());
            userService.updatePassword(userId, userr.getPassword());

            if (userr.getRoles() != null) {
                return "redirect:/admin/edit/userId ?success=2=1";

            } else {
                return "redirect:/users/edit/?success=1";
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println(e.getMessage());
            return "redirect:/users/edit/?error=1";
        }

    }

    /**
     * Maneja la solicitud POST para actualizar la información opcional del usuario
     * (Datos Opcionales).
     *
     * @param userinfo El objeto UserInfo que contiene la información opcional del
     *                 usuario.
     * @param result   El objeto BindingResult que contendrá los resultados de la
     *                 validación.
     * @param model    El objeto Model que se utilizará para pasar atributos a la
     *                 vista.
     * @return Una redirección a la página de edición del usuario con un mensaje de
     *         éxito o error.
     */
    @PostMapping("/post/settingsoptional")
    public String postOptional(@Valid @ModelAttribute("userinfo") UserInfo userinfo,
            BindingResult result, Model model) {

        Role role = RoleRepository.fetchRoleById(userinfo.getId());
        model.addAttribute("role", role);

        String userId = userinfo.getId().toString();

        try {
            userInfoService.save(userinfo);

            if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_SUPERADMIN")) {
                return "redirect:/admin/edit/userId?success=2=1";

            } else {
                return "redirect:/users/edit/?success=1";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/users/edit/?error=1";
        }

    }

    /**
     * Maneja la solicitud GET para mostrar la página del plan del usuario.
     *
     * @param model El objeto Model que se utilizará para pasar atributos a la
     *              vista.
     * @return El nombre de la vista de la página del plan del usuario.
     */
    @GetMapping("/users/plan/")
    public String showPlan(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);
        // model.addAttribute("userIMG",
        // domain + "upload/" + userServerId + "/pfp/profile.jpg");

        return "/plan";
    }

    /**
     * Maneja la solicitud GET para mostrar los resultados de búsqueda de archivos.
     *
     * @param model  El objeto Model que se utilizará para pasar atributos a la
     *               vista.
     * @param search La cadena de búsqueda que se utilizará para buscar archivos.
     * @return El nombre de la vista de la página de resultados de búsqueda.
     */
    @GetMapping("/users/search/{search}")
    public String showSearch(Model model, @PathVariable String search) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

        // Se añade el objeto user a thymeleaf
        User user = userService.getUserById(userServerId);
        model.addAttribute("user", user);

        List<File> files = fileService.findFilesByFilename(search);
        model.addAttribute("files", files);

        logWriter.writeLog("El usuario con id '" + userServerId + "' ha buscado '" + search + "'.");

        return "/search";
    }

    @GetMapping("/terms")
    public String terms() {
        return "/terminos";
    }

    @GetMapping("/faqs")
    public String faqs() {
        return "/faqs";
    }

    @GetMapping("/us")
    public String weare() {
        return "/weare";
    }

    /**
     * Maneja la solicitud GET para degradar a un usuario a un plan básico.
     *
     * @param id El ID del usuario que se quiere degradar.
     * @return Una redirección a la página del plan del usuario.
     */
    @GetMapping("/users/plan/basic/{id}")
    public String planBasic(@PathVariable Long id) {
        Long newRole = 4L;
        userService.updateRole(id, newRole);
        logWriter.writeLog(
                "El usuario con id '" + id + "' ha sido degradado a usuario Basico.");
        return "redirect:/users/plan/";
    }

    /**
     * Maneja la solicitud GET para ascender a un usuario a un plan Premium.
     *
     * @param id El ID del usuario que se quiere ascender.
     * @return Una redirección a la página del plan del usuario.
     */
    @GetMapping("/users/plan/premium/{id}")
    public String planPremium(@PathVariable Long id) {
        Long newRole = 3L;
        userService.updateRole(id, newRole);
        logWriter.writeLog(
                "El usuario con id '" + id + "' ha sido ascendido a usuario Premium.");
        return "redirect:/users/plan/";
    }
}
