package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTPayloadMapper;
import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.dtos.mqtt.dht11.DHT11Data;
import com.laboratorio.iot.plantix.dtos.mqtt.dht11.DHT11MQTTInputDTO;
import com.laboratorio.iot.plantix.dtos.mqtt.MQTTInputDTO;
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
    public <T> SensorHistory save(SensorHistory sensorHistory, T dto) throws InvalidSensorHistoryException {
        //validaciones validas para cualquier tipo de MQTTInputDTO
        if(SensorHistoryValidator.thisSensorIsNotValid(sensorHistory.getSensor()))
            throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Sensor is null.");
        if(SensorHistoryValidator.thisTimestampIsNotValid(sensorHistory.getTimestamp()))
            throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Timestamp is null.");
        //cada validacion varia del dto, ya que cada dto recibe data distinta
        //aca agregar la validacion necesaria sobre la data recibida e insertar data en el SensorHistory recibido
        if(dto instanceof DHT11MQTTInputDTO dht11MQTTInputDTO) {
            DHT11Data data = dht11MQTTInputDTO.getData();
            if(SensorHistoryValidator.thisDHT11DataIsNotValid(data))
                throw new InvalidSensorHistoryException("Failed to save given SensorHistory. Provided Data is empty or has an invalid format.");
            sensorHistory.setData(data.getTemperature()+","+data.getHumidity());
        }
        return sensorHistoryRepository.save(sensorHistory);
    }

    @Override
    public <T> void save(String jsonData, Class<T> clazz, String sensorType) throws MQTTInvalidPayloadException, SensorNotFoundException, InvalidSensorException, InvalidSensorHistoryException {
        //intento hacer el mapeo de json a una clase java o___o
        T dto = mqttPayloadMapper.mapToThisClass(jsonData, clazz);

        //intento recuperar de la bd el sensor con el id que nos llegó en el json c:
        //este casteo es seguro porque el tipo T recibido debe obligatoriamente extender MQTTInputDTO
        Sensor sensorFromDB = sensorService.findById(((MQTTInputDTO) dto).getSensorId());

        //verificamos si el sensor traido es del tipo que esperamos :3
        if(!sensorFromDB.getName().equals(sensorType))
            throw new InvalidSensorException("Received Sensor is not a "+sensorType+".");

        //preparamos el SensorHistory para registrar la nueva medicion O__o
        SensorHistory sensorHistory = new SensorHistory();
        sensorHistory.setSensor(sensorFromDB);
        sensorHistory.setTimestamp(((MQTTInputDTO) dto).getTimestamp());
        save(sensorHistory, dto);
    }

}
