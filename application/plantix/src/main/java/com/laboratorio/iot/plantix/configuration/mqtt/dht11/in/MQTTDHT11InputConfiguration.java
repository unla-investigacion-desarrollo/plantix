package com.laboratorio.iot.plantix.configuration.mqtt.dht11.in;

import com.laboratorio.iot.plantix.constants.SensorType;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.dtos.mqtt.dht11.DHT11MQTTInputDTO;
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
public class MQTTDHT11InputConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(MQTTDHT11InputConfiguration.class);
    private final ISensorHistoryService sensorHistoryService;
    public MQTTDHT11InputConfiguration(ISensorHistoryService sensorHistoryService) {
        this.sensorHistoryService = sensorHistoryService;
    }

    @Bean
    public MessageChannel dht11InputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.DHT11_CHANNEL)
    public void dht11HandleMessage(Message<?> message) {
        //extraemos el payload (json con los datos) del mensaje que nos llegó t-t
        String jsonData = message.getPayload().toString();

        //despues simplemente registramos en la bd la medicion del dht11 que nos llegó del esp32
        try {
            sensorHistoryService.save(jsonData, DHT11MQTTInputDTO.class, SensorType.DHT11);
        } catch (MQTTInvalidPayloadException | SensorNotFoundException | InvalidSensorException |
                 InvalidSensorHistoryException exception) {
            LOGGER.error("Failed to save received data from this topic: "+ MQTTBrokerInformation.DHT11_TOPIC +".", exception);
            return;
        }

        LOGGER.info("This data was successfully saved in sensor_histories table:\n"+jsonData);
    }
}
