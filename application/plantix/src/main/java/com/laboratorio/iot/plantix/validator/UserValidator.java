package com.laboratorio.iot.plantix.validator;

import java.util.regex.Pattern;

public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final int VALID_DNI_LENGTH = 8;
    public static boolean thisEmailIsValid(String email) {
        return !email.isBlank() && Pattern.matches(EMAIL_REGEX, email);
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
