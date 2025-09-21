package com.laboratorio.iot.plantix.exceptions.sensor;

public class SensorNotFoundException extends RuntimeException {
    public SensorNotFoundException(String message) {
        super(message);
    }
}
