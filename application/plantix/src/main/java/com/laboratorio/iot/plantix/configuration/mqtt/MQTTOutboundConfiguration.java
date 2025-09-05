package com.laboratorio.iot.plantix.configuration.mqtt;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MQTTOutboundConfiguration {

    private static final String CLIENT_ID = "plantix-publisher";

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(DefaultMqttPahoClientFactory clientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                CLIENT_ID, 
                clientFactory
        );
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(MQTTBrokerInformation.DEFAULT_SENSOR_QOS);
        return messageHandler;
    }

    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { MQTTBrokerInformation.URL });
        if (MQTTBrokerInformation.USERNAME != null) {
            options.setUserName(MQTTBrokerInformation.USERNAME);
        }
        if (MQTTBrokerInformation.PASSWORD != null) {
            options.setPassword(MQTTBrokerInformation.PASSWORD.toCharArray());
        }
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        return options;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
}
