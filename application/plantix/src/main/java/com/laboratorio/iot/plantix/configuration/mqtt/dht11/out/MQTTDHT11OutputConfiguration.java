package com.laboratorio.iot.plantix.configuration.mqtt.dht11.out;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTOutboundConfiguration;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MQTTDHT11OutputConfiguration {
    private final MqttPahoClientFactory mqttPahoClientFactory;

    public MQTTDHT11OutputConfiguration(MqttPahoClientFactory mqttPahoClientFactory) {
        this.mqttPahoClientFactory = mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel dht11TempOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel dht11HumidityOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.DHT11_TEMP_CHANNEL)
    public MessageHandler dht11TempOutbound() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                mqttPahoClientFactory,
                MQTTBrokerInformation.DHT11_TEMP_TOPIC
        );
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTOutputChannelInformation.DHT11_HUMIDITY_CHANNEL)
    public MessageHandler dht11HumidityOutbound() {
        return MQTTOutboundConfiguration.constructMessageHandler(
                mqttPahoClientFactory,
                MQTTBrokerInformation.DHT11_HUMIDITY_TOPIC
        );
    }
}
