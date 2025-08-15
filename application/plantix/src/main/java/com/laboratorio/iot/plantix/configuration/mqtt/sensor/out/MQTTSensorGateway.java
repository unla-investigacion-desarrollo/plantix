package com.laboratorio.iot.plantix.configuration.mqtt.sensor.out;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = MQTTOutputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL)
public interface MQTTSensorGateway {
    void sendToMqtt(String data);
}
