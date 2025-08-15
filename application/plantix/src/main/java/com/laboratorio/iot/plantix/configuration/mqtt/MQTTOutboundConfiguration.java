package com.laboratorio.iot.plantix.configuration.mqtt;

import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

public class MQTTOutboundConfiguration {
    public static MqttPahoMessageHandler constructMessageHandler(MqttPahoClientFactory mqttPahoClientFactory, String topic) {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("publisherClient", mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }
}
