package com.laboratorio.iot.plantix.configuration.mqtt.electrovalve.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.ELECTROVALVE_OPEN_CHANNEL)
public interface MQTTElectrovalveOpenGateway {
    void sendToMqtt(String data);
}
