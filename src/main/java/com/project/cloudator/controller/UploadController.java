package com.project.cloudator.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {
	// destination folder to upload the files
	private static String UPLOAD_FOLDER = "C://CloudatorFiles//UPLOAD//";

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
			// Crea el path del archivo
			Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
			// Escribe el archivo en el path
			Files.write(path, bytes);

		} catch (IOException e) {
			e.getMessage();
		}

		return new ModelAndView("status", "message", "Archivo subido correctamente");
	}
}