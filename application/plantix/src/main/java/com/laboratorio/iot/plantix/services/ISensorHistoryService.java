package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;


import java.util.List;

public interface ISensorHistoryService {
    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception;
    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) ;
    <T> SensorHistory save(SensorHistory sensorHistory, T dto) throws InvalidSensorHistoryException;
    <T> void save(String jsonData, Class<T> clazz, String sensorType) throws MQTTInvalidPayloadException, SensorNotFoundException, InvalidSensorException, InvalidSensorHistoryException;
}
