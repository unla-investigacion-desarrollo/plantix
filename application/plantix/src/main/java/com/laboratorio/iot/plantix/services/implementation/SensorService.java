package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.dtos.SensorDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.repositories.ISensorRepository;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import com.laboratorio.iot.plantix.services.ISensorService;
import com.laboratorio.iot.plantix.validator.SensorValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService implements ISensorService {
    private final ISensorRepository sensorRepository;
    private final ISensorHistoryService sensorHistoryService;

    public SensorService(ISensorRepository sensorRepository, ISensorHistoryService sensorHistoryService){
        this.sensorRepository = sensorRepository;
        this.sensorHistoryService = sensorHistoryService;
    }

    @Override
    public Sensor save(Sensor sensor) throws InvalidSensorException {
        if(SensorValidator.thisNameIsNotValid(sensor.getName()))
            throw new InvalidSensorException("Failed to save given Sensor. Property 'name' can not be empty or null.");
        if(SensorValidator.thisFieldIsNotValid(sensor.getField()))
            throw new InvalidSensorException("Failed to save given Sensor. Property 'field' can not be null.");
        if(SensorValidator.thisMeasurementIntervalIsNotValid(sensor.getMeasurementInterval()))
            throw new InvalidSensorException("Failed to save given Sensor. Property 'measurementInterval' can not be less tan zero.");
        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor findById(long id) throws SensorNotFoundException {
        return sensorRepository.findById(id).orElseThrow(() ->
                new SensorNotFoundException("Sensor with id "+id+" does not exist.")
        );
    }

    public List<SensorDTO> findAllSensorsByFieldId(Long fieldId){
        List<Sensor> sensores = sensorRepository.findAllByFieldId(fieldId);

        return sensores.stream()
                .map(sensor -> {
                    try {
                        return new SensorDTO(
                                sensor.getId(),
                                sensor.getName(),
                                sensor.getField().getId(),
                                sensor.getMeasurementInterval(),
                                sensorHistoryService.getLastSensorHistoryBySensorId(sensor.getId()).getData()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to map Sensor to SensorDTO: " + e.getMessage(), e);
                    }
                }).toList();
    }



}
