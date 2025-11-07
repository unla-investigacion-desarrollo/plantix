package com.laboratorio.iot.plantix.configuration.mqtt.sensor.in;

import com.laboratorio.iot.plantix.constants.SensorType;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.dtos.mqtt.sensor.SubstrateMoistureMQTTInputDTO;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MQTTSensorInputConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTSensorInputConfiguration.class);
    private final ISensorHistoryService sensorHistoryService;

    public MQTTSensorInputConfiguration(ISensorHistoryService sensorHistoryService) {
        this.sensorHistoryService = sensorHistoryService;
    }

    @Bean
    public MessageChannel sensorSubstrateMoistureInputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL)
    public void sensorHandleMessage(Message<?> message) {
        String jsonData = message.getPayload().toString();
        try {
            sensorHistoryService.save(jsonData, SubstrateMoistureMQTTInputDTO.class, SensorType.HW390);
        } catch (MQTTInvalidPayloadException | SensorNotFoundException | InvalidSensorException |
                 InvalidSensorHistoryException exception) {
            LOGGER.error("Failed to save received data from topic: {}.", MQTTBrokerInformation.SENSOR_SUBSTRATE_MOISTURE_TOPIC, exception);
            return;
        }
        LOGGER.info("HW390 data saved: {}", jsonData);
    }
}
