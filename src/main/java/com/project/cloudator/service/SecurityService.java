package com.project.cloudator.service;

import com.project.cloudator.utils.Decryptor;
import com.project.cloudator.utils.Encryptor;

public class SecurityService {

    private final Encryptor encryptor;
    private final Decryptor decryptor;

    /**
     * Constructor de la clase SecurityService.
     *
     * @param SECRET_KEY_ENCRYPTOR La clave secreta para el encriptador y
     *                             desencriptador.
     */
    public SecurityService(String SECRET_KEY_ENCRYPTOR) {
        this.encryptor = new Encryptor(SECRET_KEY_ENCRYPTOR);
        this.decryptor = new Decryptor(SECRET_KEY_ENCRYPTOR);
    }

    /**
     * Encripta los datos proporcionados.
     *
     * @param data Los datos que se quieren encriptar.
     * @return Los datos encriptados.
     */
    public String encryptData(String data) {
        return encryptor.encrypt(data);
    }

    /**
     * Desencripta los datos proporcionados.
     *
     * @param encryptedData Los datos encriptados que se quieren desencriptar.
     * @return Los datos desencriptados.
     */
    public String decryptData(String encryptedData) {
        return decryptor.decrypt(encryptedData);
    }
}
