package com.example.registrationlogindemo.functions;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Regex {
    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$;._\\-*]).{8,64}$";
        return Pattern.matches(passwordPattern, password);
    }

    public boolean isValidMail(String mail) {
        String mailPattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return Pattern.matches(mailPattern, mail);
    }

    public boolean isValidUsername(String username) {
        String usernamePattern = "^(?:(?=.*[0-9])|(?=.*[a-z])|(?=.*[A-Z]))[^\s!@#$%+-?¿ªº&*(),.?:{}|<>]{1,32}$";
        return Pattern.matches(usernamePattern, username);
    }
}
