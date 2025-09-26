package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.dtos.mqtt.DHT11MQTTInputDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;
import com.laboratorio.iot.plantix.exceptions.sensorhistory.InvalidSensorHistoryException;
import org.slf4j.Logger;

import java.util.List;

public interface ISensorHistoryService {
    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception;
    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) ;
    SensorHistory save(SensorHistory sensorHistory) throws InvalidSensorHistoryException;
    void save(String jsonData) throws MQTTInvalidPayloadException, SensorNotFoundException, InvalidSensorException, InvalidSensorHistoryException;
}
