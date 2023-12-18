package com.project.cloudator.functions;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Regex {
    /**
     * Verifica si una contraseña es válida según los siguientes criterios:
     * - Debe contener al menos un dígito.
     * - Debe contener al menos una letra minúscula.
     * - Debe contener al menos una letra mayúscula.
     * - Debe contener al menos uno de los siguientes caracteres especiales: $, ;, ., _, -, *.
     * - Debe tener una longitud mínima de 8 caracteres y una longitud máxima de 64 caracteres.
     *
     * @param password La contraseña a verificar.
     * @return true si la contraseña es válida, false de lo contrario.
     */
    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$;._\\-*]).{8,64}$";
        return Pattern.matches(passwordPattern, password);
    }

    /**
     * Verifica si una dirección de correo electrónico es válida según el estándar RFC 5322.
     *
     * @param mail La dirección de correo electrónico a verificar.
     * @return true si la dirección de correo electrónico es válida, false de lo contrario.
     */
    public boolean isValidMail(String mail) {
        String mailPattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return Pattern.matches(mailPattern, mail);
    }

    /**
     * Verifica si una cadena cumple con los siguientes criterios:
     * - Debe tener una longitud mínima de 1 caracter y una longitud máxima de 32 caracteres.
     * - Solo puede contener números o letras.
     *
     * @param input La cadena a verificar.
     * @return true si la cadena cumple con los criterios, false de lo contrario.
     */
    public boolean isValidUsername(String input) {
        String inputPattern = "^[a-zA-Z0-9]{1,32}$";
        return Pattern.matches(inputPattern, input);
    }
}
