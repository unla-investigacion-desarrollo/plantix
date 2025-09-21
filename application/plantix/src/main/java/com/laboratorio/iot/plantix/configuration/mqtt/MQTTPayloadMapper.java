package com.laboratorio.iot.plantix.configuration.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import org.springframework.stereotype.Component;

@Component
public class MQTTPayloadMapper {
    private final ObjectMapper objectMapper;

    public MQTTPayloadMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T mapToThisClass(String payload, Class<T> targetClass) throws MQTTInvalidPayloadException {
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
