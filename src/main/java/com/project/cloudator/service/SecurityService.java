package com.project.cloudator.service;

import com.project.cloudator.utils.Decryptor;
import com.project.cloudator.utils.Encryptor;

public class SecurityService {

    private final Encryptor encryptor;
    private final Decryptor decryptor;

    public SecurityService(String secretKey) {
        this.encryptor = new Encryptor(secretKey);
        this.decryptor = new Decryptor(secretKey);
    }

    public String encryptData(String data) {
        return encryptor.encrypt(data);
    }

    public String decryptData(String encryptedData) {
        return decryptor.decrypt(encryptedData);
    }
}
