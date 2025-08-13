package com.laboratorio.iot.plantix.configuration.mqtt.sensor.in;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MQTTSensorInputConfiguration {
    @Bean
    public MessageChannel sensorSubstrateMoistureInputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL)
    public void sensorHandleMessage(Message<?> message) {
        System.out.println("["+MQTTInputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL+"] Recibí este mensaje: "+message.getPayload());
    }
}
