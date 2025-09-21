package com.laboratorio.iot.plantix.controllers;

import com.laboratorio.iot.plantix.dtos.FieldDTO;
import com.laboratorio.iot.plantix.dtos.SensorDTO;
import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.helpers.ViewRouterHelper;
import com.laboratorio.iot.plantix.services.IFieldService;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import com.laboratorio.iot.plantix.services.ISensorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fields")
public class FieldController {
    private final IFieldService fieldService;
    private final ISensorHistoryService sensorHistoryService;
    private final ISensorService sensorService;
    public FieldController(IFieldService fieldService, ISensorHistoryService sensorHistoryService, ISensorService sensorService) {
        this.fieldService = fieldService;
        this.sensorHistoryService = sensorHistoryService;
        this.sensorService = sensorService;
    }

    @GetMapping("/list")
    public String getAllFields(Model model) throws Exception {
        List<FieldDTO> fieldsDtos = fieldService.findAllFields();
        List<SensorDTO> sensorsDtos = new ArrayList<SensorDTO>();
        for (FieldDTO field : fieldsDtos) {
            sensorsDtos.addAll(sensorService.findAllSensorsByFieldId(field.getId()));
        }

        model.addAttribute("fields", fieldsDtos);
        model.addAttribute("sensors", sensorsDtos);

        return ViewRouterHelper.LIST; // Retorna la vista fields/list.html ubicada en src/main/resources/templates/fields
    }
    @GetMapping("/{fieldId}/details")
    public String getFieldDetails(@PathVariable("fieldId") Long fieldId,
                                  Model model) throws Exception {
        FieldDTO fieldDTO = fieldService.findFieldById(fieldId);
        List<SensorDTO> sensors = sensorService.findAllSensorsByFieldId(fieldId);
        List<SensorHistoryDTO> sensorHistories = new ArrayList<SensorHistoryDTO>();
        for (SensorDTO sensor : sensors) {
            sensorHistories.addAll(sensorHistoryService.getLast10SensorHistoryBySensorId(sensor.getId()));
        }

        model.addAttribute("field", fieldDTO);
        model.addAttribute("sensors", sensors);
        model.addAttribute("sensorHistories", sensorHistories);

        return ViewRouterHelper.DETAIL;
    }
}
