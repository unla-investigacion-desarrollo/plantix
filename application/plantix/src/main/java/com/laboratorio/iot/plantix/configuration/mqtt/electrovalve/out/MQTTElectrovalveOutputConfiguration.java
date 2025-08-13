package com.laboratorio.iot.plantix.configuration.mqtt.electrovalve.out;

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
public class MQTTElectrovalveOutputConfiguration {
    private final MqttPahoClientFactory mqttPahoClientFactory;

    public MQTTElectrovalveOutputConfiguration(MqttPahoClientFactory mqttPahoClientFactory) {
        this.mqttPahoClientFactory = mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel electrovalveOpenOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel electrovalveCloseOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.ELECTROVALVE_OPEN_CHANNEL)
    public MessageHandler electrovalveOpenOutbound() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                mqttPahoClientFactory,
                MQTTBrokerInformation.ELECTROVALVE_OPEN_TOPIC
        );
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL)
    public MessageHandler electrovalveCloseOutbound() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                mqttPahoClientFactory,
                MQTTBrokerInformation.ELECTROVALVE_CLOSE_TOPIC
        );
    }
}
