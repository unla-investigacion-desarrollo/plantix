package com.laboratorio.iot.plantix.configuration.mqtt.hw390.out;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTOutboundConfiguration;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttHw390OutputConfiguration {

    private final MqttPahoClientFactory mqttPahoClientFactory;

    public MqttHw390OutputConfiguration(MqttPahoClientFactory mqttPahoClientFactory) {
        this.mqttPahoClientFactory = mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel hw390ConfigurationOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.HW390_CONFIGURATION_CHANNEL)
    public MessageHandler mqttOutboundHw390Config() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                this.mqttPahoClientFactory,
                MQTTBrokerInformation.HW390_CONFIGURATION_TOPIC
        );
    }
}

