package com.laboratorio.iot.plantix.configuration.mqtt.electrovalve.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL)
public interface MQTTElectrovalveCloseGateway {
    void sendToMqtt(String data);
}
