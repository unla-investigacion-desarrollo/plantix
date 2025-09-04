package com.laboratorio.iot.plantix.configuration.mqtt.dht11.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.DHT11_CHANNEL)
public interface MQTTDHT11Gateway {
    void sendToMqtt(String data);
}
