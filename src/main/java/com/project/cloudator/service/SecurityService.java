package com.project.cloudator.service;

import org.springframework.stereotype.Service;
import com.project.cloudator.utils.Decryptor;
import com.project.cloudator.utils.Encryptor;

public class SecurityService {

    private final Encryptor encryptor;
    private final Decryptor decryptor;

    public SecurityService(String SECRET_KEY_ENCRYPTOR) {
        this.encryptor = new Encryptor(SECRET_KEY_ENCRYPTOR);
        this.decryptor = new Decryptor(SECRET_KEY_ENCRYPTOR);
    }

    public String encryptData(String data) {
        return encryptor.encrypt(data);
    }

    public String decryptData(String encryptedData) {
        return decryptor.decrypt(encryptedData);
    }
}
