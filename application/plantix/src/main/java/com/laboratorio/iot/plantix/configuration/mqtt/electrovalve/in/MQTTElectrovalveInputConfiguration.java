package com.laboratorio.iot.plantix.configuration.mqtt.electrovalve.in;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MQTTElectrovalveInputConfiguration {
    @Bean
    public MessageChannel electrovalveOpenInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel electrovalveCloseInputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.ELECTROVALVE_OPEN_CHANNEL)
    public void electrovalveOpenHandleMessage(Message<?> message) {
        System.out.println("["+MQTTInputChannelInformation.ELECTROVALVE_OPEN_CHANNEL+"] Recibí este mensaje: "+message.getPayload());
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL)
    public void electrovalveCloseHandleMessage(Message<?> message) {
        System.out.println("["+MQTTInputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL+"] Recibí este mensaje: "+message.getPayload());
    }
}
