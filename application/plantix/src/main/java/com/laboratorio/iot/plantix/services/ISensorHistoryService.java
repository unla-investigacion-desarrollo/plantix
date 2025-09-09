package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;

import java.util.List;

public interface ISensorHistoryService {
    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception;
    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) ;
}
