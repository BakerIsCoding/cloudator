package com.project.cloudator.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
	// destination folder to upload the files
	private static String UPLOAD_FOLDER = "C://CloudatorFiles//UPLOAD//";
	private static final String STORAGE_SERVER_URL = "https://4a8f-91-126-76-58.ngrok-free.app/upload/file";

	@GetMapping("/upload")
	public String showViewUpload() {
		return "upload";
	}

	@RequestMapping("/upload")
	public ModelAndView showUpload() {
		return new ModelAndView("upload");
	}

	@PostMapping("/upload")
	public ModelAndView fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

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
			body.add("owner", "3");

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(STORAGE_SERVER_URL, requestEntity,
					String.class);
		} catch (IOException e) {
			return new ModelAndView("status", "message", "Error al procesar el archivo");
		}

		return new ModelAndView("status", "message", "Archivo subido correctamente");
	}

	@PostMapping("/settings/image/post")
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
}