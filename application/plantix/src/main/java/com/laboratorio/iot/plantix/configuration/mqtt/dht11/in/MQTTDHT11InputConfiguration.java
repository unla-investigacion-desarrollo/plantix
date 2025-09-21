package com.laboratorio.iot.plantix.configuration.mqtt.dht11.in;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTPayloadMapper;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.dtos.mqtt.DHT11MQTTInputDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import com.laboratorio.iot.plantix.services.ISensorService;
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
    private final MQTTPayloadMapper mqttPayloadMapper;
    private final ISensorService sensorService;
    private final ISensorHistoryService sensorHistoryService;
    public MQTTDHT11InputConfiguration(MQTTPayloadMapper mqttPayloadMapper, ISensorService sensorService, ISensorHistoryService sensorHistoryService) {
        this.mqttPayloadMapper = mqttPayloadMapper;
        this.sensorService = sensorService;
        this.sensorHistoryService = sensorHistoryService;
    }

    @Bean
    public MessageChannel dht11InputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.DHT11_CHANNEL)
    public void dht11HandleMessage(Message<?> message) {
        String jsonData = message.getPayload().toString();

        //intento hacer el mapeo de json a una clase java o___o
        DHT11MQTTInputDTO dht11MQTTInputDTO = processJson(jsonData);
        if(dht11MQTTInputDTO == null) return;

        //intento recuperar de la bd el sensor con el id que nos llegó en el json c:
        Sensor sensorFromDB = findSensorFromDB(dht11MQTTInputDTO.getSensorId());
        if(sensorFromDB == null) return;

        //despues simplemente registramos en la bd la medicion del dht11 que nos llegó del esp32
        boolean successfullySaved = saveSensorHistory(dht11MQTTInputDTO, sensorFromDB);
        if(successfullySaved)
            LOGGER.info("This data was successfully saved in sensor_histories table:\n"+jsonData);
    }

    private DHT11MQTTInputDTO processJson(String jsonData) {
        try {
            return mqttPayloadMapper.mapToThisClass(jsonData, DHT11MQTTInputDTO.class);
        } catch (MQTTInvalidPayloadException mqttInvalidPayloadException) {
            //lo mejor es printear el error y retornar, en vez de lanzar otra excepcion
            //si lanzamos una excepcion en este punto de la ejecucion provocariamos que el sistema se desconecte por completo del broker mqtt
            LOGGER.error("Failed to parse given JSON to expected class.", mqttInvalidPayloadException);
            return null;
        }
    }

    private Sensor findSensorFromDB(long sensorId) {
        try {
            return sensorService.findById(sensorId);
        } catch (SensorNotFoundException sensorNotFoundException) {
            //misma idea que el metodo de arriba. no lanzamos excepcion para no desconectarnos del broker :D
            LOGGER.error("Couldn't find expected Sensor.", sensorNotFoundException);
            return null;
        }
    }

    private boolean saveSensorHistory(DHT11MQTTInputDTO dht11MQTTInputDTO, Sensor sensorFromDB) {
        SensorHistory sensorHistory = new SensorHistory();
        sensorHistory.setSensor(sensorFromDB);
        sensorHistory.setTimestamp(dht11MQTTInputDTO.getTimestamp());
        sensorHistory.setData(dht11MQTTInputDTO.getData());
        try {
            sensorHistoryService.save(sensorHistory);
            return true;
        } catch (InvalidSensorHistoryException invalidSensorHistoryException) {
            LOGGER.error("Failed to save given SensorHistory.", invalidSensorHistoryException);
            return false;
        }
    }
}
