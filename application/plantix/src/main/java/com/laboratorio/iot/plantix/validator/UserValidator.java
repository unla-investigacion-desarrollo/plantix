package com.laboratorio.iot.plantix.validator;

import java.util.regex.Pattern;

public class UserValidator {
    private static String GMAIL_REGEX = "\\*.@gmail.com";
    private static String HOTMAIL_REGEX = "\\*.@hotmail.com";
    private static int VALID_DNI_LENGTH = 8;
    public static boolean thisEmailIsValid(String email) {
        boolean valid = true;

        if(email.isBlank()) valid = false;
        if(email.contains("@gmail.com") && !Pattern.matches(GMAIL_REGEX, email)) valid = false;
        if(email.contains("@hotmail.com") && !Pattern.matches(HOTMAIL_REGEX, email)) valid = false;

        return valid;
    }
    public static boolean thisDNIIsValid(long dni) {
        boolean valid = true;
        if(String.valueOf(dni).isBlank()) valid = false;
        if(String.valueOf(dni).length() != VALID_DNI_LENGTH) valid = false;
        if(dni < 0) valid = false;
        return valid;
    }
    public static boolean thisPasswordIsValid(String password) {
        return !password.isBlank();
    }
}
