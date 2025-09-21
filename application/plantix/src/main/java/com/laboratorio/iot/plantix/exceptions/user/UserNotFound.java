package com.laboratorio.iot.plantix.exceptions.user;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
