package com.laboratorio.iot.plantix.configuration.mqtt.dht11.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.DHT11_TEMP_CHANNEL)
public interface MQTTDHT11TempGateway {
    void sendToMqtt(String data);
}
