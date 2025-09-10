package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.dtos.FieldDTO;
import com.laboratorio.iot.plantix.entities.Field;
import com.laboratorio.iot.plantix.repositories.IFieldRepository;
import com.laboratorio.iot.plantix.services.IFieldService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldService implements IFieldService{

    private final IFieldRepository fieldRepository;

    public FieldService(IFieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public List<FieldDTO> findAllFields() {
        List<Field> fields = fieldRepository.findAll();
        return fields.stream().map(field -> new FieldDTO(
                field.getId(),
                field.getLocation(),
                field.getPhValue(),
                field.getPhValueMin(),
                field.getPhValueMax(),
                field.getSubstrateHumidity(),
                field.getSubstrateHumidityMin(),
                field.getSubstrateHumidityMax(),
                field.getTemperature(),
                field.getTemperatureMin(),
                field.getTemperatureMax(),
                field.getHumidity(),
                field.getHumidityMin(),
                field.getHumidityMax(),
                field.getLight(),
                field.getLightMin(),
                field.getLightMax(),
                field.getNotes()
        )).toList();
    }
    public FieldDTO findFieldById(Long fieldId) throws Exception {
        Field field = fieldRepository.findById(fieldId).orElseThrow(()-> new Exception("Field not found with id: " + fieldId));
        return new FieldDTO(
                field.getId(),
                field.getLocation(),
                field.getPhValue(),
                field.getPhValueMin(),
                field.getPhValueMax(),
                field.getSubstrateHumidity(),
                field.getSubstrateHumidityMin(),
                field.getSubstrateHumidityMax(),
                field.getTemperature(),
                field.getTemperatureMin(),
                field.getTemperatureMax(),
                field.getHumidity(),
                field.getHumidityMin(),
                field.getHumidityMax(),
                field.getLight(),
                field.getLightMin(),
                field.getLightMax(),
                field.getNotes()
        );
    }
}
