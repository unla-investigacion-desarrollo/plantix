package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.dtos.FieldDTO;

import java.util.List;

public interface IFieldService {
    public List<FieldDTO> findAllFields();

    public FieldDTO findFieldById(Long fieldId) throws Exception;
}
