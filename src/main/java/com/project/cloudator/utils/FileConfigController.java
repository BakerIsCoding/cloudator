package com.project.cloudator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileConfigController {

    private String domain;
    private String configFilePath = "config.cfg";

    public FileConfigController() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new IOException("Configuration file '" + configFilePath + "' not found in the classpath");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2 && "domain".equals(parts[0].trim())) {
                    domain = parts[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Maneja el error de acuerdo a tus necesidades
        }
    }

    public String getDomain() {
        return domain;
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        FileConfigController configController = new FileConfigController();
        String domain = configController.getDomain();

        System.out.println("Domain: " + domain);
    }
}