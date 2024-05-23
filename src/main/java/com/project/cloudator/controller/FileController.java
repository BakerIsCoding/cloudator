package com.project.cloudator.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Value;

import com.project.cloudator.entity.File;
import com.project.cloudator.entity.Role;
import com.project.cloudator.entity.User;
import com.project.cloudator.functions.LogWriter;
import com.project.cloudator.jwt.JsonWebTokenManager;
import com.project.cloudator.repository.UserRoleRepository;
import com.project.cloudator.service.FileService;
import com.project.cloudator.service.UserService;
import com.project.cloudator.functions.LogWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FileController {

	@Autowired
	private JsonWebTokenManager jwtManager;

	@Autowired
	private FileService fileService;

	@Autowired
	private LogWriter logWriter;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private UserService userService;
	// destination folder to upload the files
	// private static String UPLOAD_FOLDER = "C://CloudatorFiles//UPLOAD//";
	// private static final String STORAGE_SERVER_URL =
	// "https://host.cloudator.live/upload/file"; //DESCOMENTAR CUANDO FUNCIONE EN
	// EL SERVIDOR.

	@Value("${domain}")
	private String domain;

	// private final String STORAGE_SERVER_URL = domain + "upload/file";
	// private final String STORAGE_SERVER_URL =
	// "https://host.cloudator.live/upload/file";
	private final String STORAGE_SERVER_URL = "http://management-pants.gl.at.ply.gg:27118/upload/file";

	@GetMapping("/upload")
	public String showViewUpload() {
		return "upload";
	}

	/**
	 * Maneja la solicitud para mostrar la página de subida de archivos.
	 *
	 * @return Un objeto ModelAndView que contiene el nombre de la vista de subida
	 *         de archivos.
	 */
	@RequestMapping("/users/upload/")
	public ModelAndView showUpload() {
		return new ModelAndView("upload");
	}

	/**
	 * Maneja la solicitud POST para actualizar la visibilidad de un archivo.
	 *
	 * @param fileId   El ID del archivo cuya visibilidad se quiere actualizar.
	 * @param isPublic Un booleano que indica si el archivo debe ser público o no.
	 * @return Una ResponseEntity con un mensaje de éxito o error.
	 */
	@PostMapping("/users/files/{fileId}/visibility")
	public ResponseEntity<String> updateFileVisibility(@PathVariable Long fileId, @RequestParam boolean isPublic) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());

		// Verificar si el usuario es el dueño del archivo
		if (!fileService.checkFileOwnership(fileId, userId)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado para actualizar este archivo");
		}

		try {
			fileService.updateFileVisibility(fileId, isPublic);
			return ResponseEntity.ok("Visibilidad del archivo actualizada con éxito");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al actualizar la visibilidad del archivo: " + e.getMessage());
		}
	}

	@GetMapping("/users/files/delete/{fileId}")
	@ResponseBody
	public ModelAndView deleteFile(@PathVariable Long fileId, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());

		try {
			// Verificar si el archivo pertenece al usuario
			boolean isOwner = fileService.checkFileOwnership(fileId, userId);
			if (!isOwner) {
				// Redirige a la página de error personalizada
				return new ModelAndView("redirect:/error401/");
			}

			// Obtener el nombre del archivo por ID
			String filename = fileService.getFileById(fileId);

			// Configurar la solicitud POST al otro endpoint
			try {
				RestTemplate restTemplate = new RestTemplate();
				// String url = "http://management-pants.gl.at.ply.gg:27118/file/delete";
				String url = "http://host.cloudator.live/file/delete";

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
				String userIdStr = Long.toString(userId);
				map.add("id", userIdStr);
				map.add("filename", filename);

				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
				ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
				System.out.println("file server response: " + response.getBody());
			} catch (Exception e) {
				logWriter.writeLog("Error al realizar la solicitud POST: " + e.getMessage());
				return new ModelAndView("redirect:/users/files/").addObject("message",
						"Error al comunicar con el servidor de archivos");
			}

			// Eliminar el archivo localmente
			fileService.deleteFile(fileId);
			logWriter.writeLog("El usuario con id '" + userId + "' ha eliminado el archivo con id '" + fileId + "'.");
			return new ModelAndView("redirect:/users/files/").addObject("message", "Archivo eliminado correctamente");
		} catch (Exception e) {
			logWriter.writeLog("Error al eliminar el archivo: " + e.getMessage());
			return new ModelAndView("redirect:/users/files/").addObject("message", "Error al eliminar el archivo");
		}
	}

	/**
	 * Maneja la solicitud POST para subir un archivo.
	 *
	 * @param file               El archivo MultipartFile que se quiere subir.
	 * @param redirectAttributes Los atributos de redirección que se utilizarán para
	 *                           enviar mensajes flash.
	 * @return Un objeto ModelAndView que contiene el nombre de la vista de subida
	 *         de archivos y los mensajes flash.
	 */
	@PostMapping("/post/upload")
	public ModelAndView fileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
		String username = userDetails.getUsername();
		Long userServerId = userService.getUserIdByUsername(username);

		if (file.isEmpty()) {
			return new ModelAndView("upload", "message", "Ha ocurrido un error: Selecciona un archivo válido");
		}

		try {
			// Lee el archivo
			byte[] bytes = file.getBytes();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new ByteArrayResource(bytes) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			});
			body.add("owner", userServerId);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(STORAGE_SERVER_URL, requestEntity,
					String.class);

			// Aquí se incluye la respuesta del servidor
			String serverResponse = responseEntity.getBody();

			// Añade la respuesta del servidor a la vista
			ModelAndView modelAndView = new ModelAndView("redirect:/users/upload/");
			redirectAttributes.addFlashAttribute("message", "Archivo subido correctamente");
			redirectAttributes.addFlashAttribute("serverResponse", "Respuesta del servidor: " + serverResponse);

			try {
				ArrayList<String> arrayItems = jwtManager.decodeJwt(serverResponse);

				String fileDateString = arrayItems.get(3);
				DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
				Date fileDate = dateFormat.parse(fileDateString);

				Long fileSizeLong = Long.parseLong(arrayItems.get(4));
				Long owner = Long.parseLong(arrayItems.get(5));
				String url = arrayItems.get(7);

				Long userStorage = fileService.getStorage(userServerId);

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

				BigInteger totalFileSize = BigInteger.valueOf(fileSizeLong);
				BigInteger totalStorageFinal = totalFileSize.add(totalStorageUsed);

				if (totalStorageFinal.compareTo(maxStorage) < 0) {

				} else {

				}

				File fileEntity = new File();

				fileEntity.setFilename(arrayItems.get(0));
				fileEntity.setFiletype(arrayItems.get(1));
				fileEntity.setFileroute(arrayItems.get(2));
				fileEntity.setFiledate(fileDate);
				fileEntity.setFilesize(fileSizeLong);
				fileEntity.setOwner(owner);
				fileEntity.setIspublic(Boolean.parseBoolean(arrayItems.get(6)));
				fileEntity.setUrl(url);

				fileService.saveFileOnDb(fileEntity);
				logWriter.writeLog("El usuario con id '" + userServerId + "' ha subido un archivo.");
			} catch (Exception e) {

				System.out.println(
						"Error en filecontroller, mensaje:" + e.getMessage() + "\nStack: ");
				e.printStackTrace();

				redirectAttributes.addFlashAttribute("message",
						"Ha ocurrido un error: Error al procesar el archivo");

				logWriter.writeLog("Error al subir el archivo: " + e.getMessage());

				return modelAndView;

			}

			return modelAndView;
		} catch (IOException e) {
			logWriter.writeLog("Error al subir el archivo: " + e.getMessage());
			return new ModelAndView("upload", "message", "Ha ocurrido un error: Error al procesar el archivo");
		}

	}

	/*
	 * private static final String EXTERNAL_FILE_PATH =
	 * "C://CloudatorFiles//DOWNLOAD//";
	 * 
	 * @RequestMapping("/download/file/{fileName}")
	 * public String downloadResource(HttpServletRequest request,
	 * HttpServletResponse response,
	 * 
	 * @PathVariable("fileName") String fileName) throws IOException {
	 * 
	 * File file = new File(EXTERNAL_FILE_PATH + fileName);
	 * if (file.exists()) {
	 * // Get MIME type of the file
	 * String mimeType = URLConnection.guessContentTypeFromName(file.getName());
	 * if (mimeType == null) {
	 * // unknown mimetype so set the mimetype to application/octet-stream
	 * mimeType = "application/octet-stream";
	 * }
	 * 
	 * response.setContentType(mimeType);
	 * response.setHeader("Content-Disposition",
	 * String.format("attachment; filename=\"" + file.getName() + "\""));
	 * response.setContentLength((int) file.length());
	 * 
	 * InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	 * FileCopyUtils.copy(inputStream, response.getOutputStream());
	 * return "redirect:/users/edit/?error01=1";
	 * 
	 * } else {
	 * return "status";
	 * // return new ModelAndView("status", "message",
	 * "Archivo subido correctamente");
	 * }
	 * }
	 */
}
