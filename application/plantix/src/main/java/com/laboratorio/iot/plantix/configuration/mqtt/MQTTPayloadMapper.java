package com.laboratorio.iot.plantix.configuration.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;

public class MQTTPayloadMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapToThisClass(String payload, Class<T> targetClass) throws MQTTInvalidPayloadException {
        if(payload == null || payload.isEmpty()) {
            throw new MQTTInvalidPayloadException("ERROR: Cannot map null or empty payload.");
        }
        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new MQTTInvalidPayloadException("ERROR: Failed to map given payload to "+targetClass+" class.", jsonProcessingException);
        }
    }
}
