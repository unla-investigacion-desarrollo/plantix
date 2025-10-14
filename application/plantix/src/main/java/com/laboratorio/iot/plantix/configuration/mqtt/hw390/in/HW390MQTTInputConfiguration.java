package com.laboratorio.iot.plantix.configuration.mqtt.hw390.in;

import com.laboratorio.iot.plantix.constants.SensorType;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.dtos.mqtt.HW390MQTTInputDTO;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.DirectChannel;

@Configuration
public class HW390MQTTInputConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(HW390MQTTInputConfiguration.class);
    private final ISensorHistoryService sensorHistoryService;

    public HW390MQTTInputConfiguration(ISensorHistoryService sensorHistoryService) {
        this.sensorHistoryService = sensorHistoryService;
    }

    @Bean
    public MessageChannel hw390InputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.HW390_CHANNEL)
    public void hw390HandleMessage(Message<?> message) {
        // Extraemos el payload (json con los datos) del mensaje que nos llegó
        String jsonData = message.getPayload().toString();

        // Registramos en la bd la medición del hw390 que nos llegó del esp32
        try {
            sensorHistoryService.save(jsonData, HW390MQTTInputDTO.class, SensorType.HW390);
        } catch (MQTTInvalidPayloadException | SensorNotFoundException | InvalidSensorException |
                 InvalidSensorHistoryException exception) {
            LOGGER.error("Failed to save received data from this topic: " + MQTTBrokerInformation.HW390_TOPIC + ".", exception);
            return;
        }

        LOGGER.info("This data was successfully saved in sensor_histories table:\n" + jsonData);
    }
}