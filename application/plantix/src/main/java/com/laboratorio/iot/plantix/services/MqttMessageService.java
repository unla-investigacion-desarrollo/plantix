package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.mqtt.*;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public interface MqttMessageService extends MessageHandler {
    void processSensorDataRequest(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
    void processDht11Data(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
    void processSubstrateMoistureData(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
    void processElectrovalveOpenCommand(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
    void processElectrovalveCloseConfirmation(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
    void processErrorMessage(String payload) throws MQTTInvalidPayloadException, JsonProcessingException;
}
