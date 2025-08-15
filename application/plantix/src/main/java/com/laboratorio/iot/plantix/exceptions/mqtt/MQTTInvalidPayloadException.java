package com.laboratorio.iot.plantix.exceptions.mqtt;

public class MQTTInvalidPayloadException extends Exception {
    public MQTTInvalidPayloadException(String message) {
        super(message);
    }
    public MQTTInvalidPayloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
