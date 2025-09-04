package com.laboratorio.iot.plantix.configuration.mqtt.dht11.in;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MQTTDHT11InputConfiguration {
    @Bean
    public MessageChannel dht11InputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.DHT11_CHANNEL)
    public void dht11TempHandleMessage(Message<?> message) {
        System.out.println("["+MQTTInputChannelInformation.DHT11_CHANNEL+"] Recibí este mensaje: "+message.getPayload());
    }
}
