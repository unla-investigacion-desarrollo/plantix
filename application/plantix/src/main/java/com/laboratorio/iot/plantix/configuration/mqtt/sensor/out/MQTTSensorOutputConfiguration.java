package com.laboratorio.iot.plantix.configuration.mqtt.sensor.out;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTOutboundConfiguration;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MQTTSensorOutputConfiguration {
    private final MqttPahoClientFactory mqttPahoClientFactory;

    public MQTTSensorOutputConfiguration(MqttPahoClientFactory mqttPahoClientFactory) {
        this.mqttPahoClientFactory = mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel sensorSubstrateMoistureOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL)
    public MessageHandler sensorSubstrateMoistureOutbound() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                mqttPahoClientFactory,
                MQTTBrokerInformation.SENSOR_SUBSTRATE_MOISTURE_TOPIC
        );
    }
}
