package com.project.cloudator.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class Encryptor {

    private AES256TextEncryptor textEncryptor;

    public Encryptor(String secretKeyEncryptor) {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKeyEncryptor);
    }

    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }
}