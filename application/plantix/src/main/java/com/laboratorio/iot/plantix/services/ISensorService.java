package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.SensorDTO;

import java.util.List;

public interface ISensorService {

    public List<SensorDTO> findAllSensorsByFieldId(Long fieldId);
}
