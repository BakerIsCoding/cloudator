package com.project.cloudator.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class Decryptor {

    private AES256TextEncryptor textEncryptor;

    public Decryptor(String secretKey) {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey);
    }

    public String decrypt(String encryptedData) {
        return textEncryptor.decrypt(encryptedData);
    }
}
