package com.laboratorio.iot.plantix.configuration.mqtt.hw390.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.HW390_CONFIGURATION_CHANNEL)
public interface MQTTHw390Gateway {

    void sendToMqtt(String data);
}

