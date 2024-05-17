package com.project.cloudator.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class Decryptor {

    private AES256TextEncryptor textEncryptor;

    /**
     * Constructor de la clase Decryptor.
     *
     * @param secretKey La clave secreta para el desencriptador.
     */
    public Decryptor(String secretKey) {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey);
    }

    /**
     * Desencripta los datos proporcionados.
     *
     * @param encryptedData Los datos encriptados que se quieren desencriptar.
     * @return Los datos desencriptados.
     */
    public String decrypt(String encryptedData) {
        return textEncryptor.decrypt(encryptedData);
    }
}
