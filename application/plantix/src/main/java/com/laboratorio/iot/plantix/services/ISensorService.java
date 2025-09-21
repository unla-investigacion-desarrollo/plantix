package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorDTO;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.exceptions.sensor.InvalidSensorException;
import com.laboratorio.iot.plantix.exceptions.sensor.SensorNotFoundException;

import java.util.List;

public interface ISensorService {
    Sensor save(Sensor sensor) throws InvalidSensorException;
    Sensor findById(long id) throws SensorNotFoundException;
    public List<SensorDTO> findAllSensorsByFieldId(Long fieldId);
}
