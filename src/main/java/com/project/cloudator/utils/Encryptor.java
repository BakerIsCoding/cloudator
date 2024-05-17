package com.project.cloudator.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class Encryptor {

    private AES256TextEncryptor textEncryptor;

    /**
     * Constructor de la clase Encryptor.
     *
     * @param secretKeyEncryptor La clave secreta para el encriptador.
     */
    public Encryptor(String secretKeyEncryptor) {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKeyEncryptor);
    }

    /**
     * Encripta los datos proporcionados.
     *
     * @param data Los datos que se quieren encriptar.
     * @return Los datos encriptados.
     */
    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }
}