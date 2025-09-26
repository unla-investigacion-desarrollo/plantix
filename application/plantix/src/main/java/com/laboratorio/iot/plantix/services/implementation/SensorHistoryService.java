package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTPayloadMapper;
import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.dtos.mqtt.DHT11MQTTInputDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;
import com.laboratorio.iot.plantix.repositories.ISensorHistoryRepository;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import com.laboratorio.iot.plantix.services.ISensorService;
import com.laboratorio.iot.plantix.validator.SensorHistoryValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;

@Service
public class SensorHistoryService implements ISensorHistoryService {
    private final ISensorHistoryRepository sensorHistoryRepository;
    private final ISensorService sensorService;
    private final MQTTPayloadMapper mqttPayloadMapper;

    public SensorHistoryService(ISensorHistoryRepository sensorHistoryRepository, @Lazy ISensorService sensorService, MQTTPayloadMapper mqttPayloadMapper) {
        this.sensorHistoryRepository = sensorHistoryRepository;
        this.sensorService = sensorService;
        this.mqttPayloadMapper = mqttPayloadMapper;
    }

    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception {
        return sensorHistoryRepository.findTopBySensorIdOrderByTimestampDesc(sensorId)
                .map(sensorHistory -> new SensorHistoryDTO(
                        sensorHistory.getId(),
                        sensorHistory.getSensor().getId(),
                        sensorHistory.getTimestamp(),
                        sensorHistory.getData()
                ))
                .orElseThrow(()-> new Exception("No sensor history found for sensor ID: " + sensorId));
    }

    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) {
        return sensorHistoryRepository.findTop10BySensorIdOrderByTimestampDesc(sensorId)
                .stream()
                .map(sensorHistory -> new SensorHistoryDTO(
                        sensorHistory.getId(),
                        sensorHistory.getSensor().getId(),
                        sensorHistory.getTimestamp(),
                        sensorHistory.getData()
                )).toList();
    }

    @Override
    public SensorHistory save(SensorHistory sensorHistory) throws InvalidSensorHistoryException {
        if(SensorHistoryValidator.thisSensorIsNotValid(sensorHistory.getSensor()))
            throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Sensor is null.");
        if(SensorHistoryValidator.thisTimestampIsNotValid(sensorHistory.getTimestamp()))
            throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Timestamp is null.");
        if(SensorHistoryValidator.thisDataIsNotValid(sensorHistory.getData()))
            throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Data is empty or has an invalid format.");
        return sensorHistoryRepository.save(sensorHistory);
    }

    @Override
    public void save(String jsonData) throws MQTTInvalidPayloadException, SensorNotFoundException, InvalidSensorException, InvalidSensorHistoryException {
        //intento hacer el mapeo de json a una clase java o___o
        DHT11MQTTInputDTO dht11MQTTInputDTO = mqttPayloadMapper.mapToThisClass(jsonData, DHT11MQTTInputDTO.class);

        //intento recuperar de la bd el sensor con el id que nos llegó en el json c:
        Sensor sensorFromDB = sensorService.findById(dht11MQTTInputDTO.getSensorId());

        //si el sensor que recibimos en el payload no es un dht11 lloramos
        if(!sensorFromDB.getName().equals("DHT11"))
            throw new InvalidSensorException("Received Sensor is not a DHT11.");

        //si nada falló, registramos la nueva medicion O__o
        SensorHistory sensorHistory = new SensorHistory();
        sensorHistory.setSensor(sensorFromDB);
        sensorHistory.setTimestamp(dht11MQTTInputDTO.getTimestamp());
        sensorHistory.setData(dht11MQTTInputDTO.getData());
        save(sensorHistory);
    }

}
