package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;

import java.util.List;

public interface ISensorHistoryService {
    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception;
    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) ;
    SensorHistory save(SensorHistory sensorHistory) throws InvalidSensorHistoryException;
}
