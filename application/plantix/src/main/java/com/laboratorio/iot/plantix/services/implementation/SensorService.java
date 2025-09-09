package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.dtos.SensorDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.repositories.ISensorHistoryRepository;
import com.laboratorio.iot.plantix.repositories.ISensorRepository;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import com.laboratorio.iot.plantix.services.ISensorService;
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
                        throw new RuntimeException(e);
                    }
                }).toList();
    }



}
