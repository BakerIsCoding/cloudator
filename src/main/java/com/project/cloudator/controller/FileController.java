package com.project.cloudator.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.cloudator.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FileController {

	@Autowired
    private UserService userService;
	// destination folder to upload the files
	private static String UPLOAD_FOLDER = "C://CloudatorFiles//UPLOAD//";
	private static final String STORAGE_SERVER_URL = "https://ad81-91-126-76-58.ngrok-free.app/upload/file";

	@GetMapping("/upload")
	public String showViewUpload() {
		return "upload";
	}

	@RequestMapping("/users/upload/{id}")
	public ModelAndView showUpload() {
		return new ModelAndView("upload");
	}

	@PostMapping("/post/upload")
	public ModelAndView fileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
        String username = userDetails.getUsername();
        Long userServerId = userService.getUserIdByUsername(username);

		if (file.isEmpty()) {
			return new ModelAndView("status", "message", "Selecciona un archivo válido");
		}

		try {
			// Lee el archivo
			byte[] bytes = file.getBytes();

			Path localPath = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());

			Files.write(localPath, bytes);

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
		} catch (IOException e) {
			return new ModelAndView("status", "message", "Error al procesar el archivo");
		}

		return new ModelAndView("status", "message", "Archivo subido correctamente");
	}

	@PostMapping("/post/settings/image")
	public ModelAndView handleFileUpload(@RequestParam("image") MultipartFile image) {
		if (image.isEmpty()) {
			return new ModelAndView("settings", "message", "Por favor, selecciona una imagen válida.");
		}

		try {
			byte[] bytes = image.getBytes();
			Path path = Paths.get(UPLOAD_FOLDER + image.getOriginalFilename());
			Files.write(path, bytes);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("image", new ByteArrayResource(bytes) {
				@Override
				public String getFilename() {
					return image.getOriginalFilename();
				}
			});

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(STORAGE_SERVER_URL, requestEntity,
					String.class);

			return new ModelAndView("settings", "message", "Imagen subida correctamente.");
		} catch (IOException e) {

			return new ModelAndView("settings", "message", "Error al subir la imagen: " + e.getMessage());
		}
	}

	private static final String EXTERNAL_FILE_PATH = "C://CloudatorFiles//DOWNLOAD//";

	@RequestMapping("/download/file/{fileName}")
	public String downloadResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {
             
		File file = new File(EXTERNAL_FILE_PATH + fileName);
		if (file.exists()) {
			//Get MIME type of the file
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			return "redirect:/users/edit/?error01=1";

		} else {
			return "status";
			//return new ModelAndView("status", "message", "Archivo subido correctamente");
		}
	}
}