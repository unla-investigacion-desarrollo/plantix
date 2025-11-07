package com.laboratorio.iot.plantix.controllers;

import com.laboratorio.iot.plantix.services.implementation.AlertServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mqtt")
public class MqttTestController {

    private final AlertServiceImpl alertService;

    public MqttTestController(AlertServiceImpl alertService) {
        this.alertService = alertService;
    }

    /**
     * Endpoint de prueba para enviar configuración MQTT al sensor HW390
     *
     * @param minValue Valor mínimo de humedad de sustrato (por defecto 15.0)
     * @param maxValue Valor máximo de humedad de sustrato (por defecto 85.0)
     * @return Respuesta JSON con el resultado de la operación
     */
    @PostMapping("/hw390/config")
    public ResponseEntity<Map<String, Object>> sendHw390Configuration(
            @RequestParam(defaultValue = "15.0") Float minValue,
            @RequestParam(defaultValue = "85.0") Float maxValue) {

        Map<String, Object> response = new HashMap<>();

        try {
            alertService.sendHw390Configuration(minValue, maxValue);

            response.put("success", true);
            response.put("message", "Configuración MQTT enviada correctamente al tópico hw390-config");
            response.put("topic", "hw390-config");
            response.put("payload", Map.of("minValue", minValue, "maxValue", maxValue));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al enviar configuración MQTT: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Endpoint GET simple para probar el envío con valores por defecto
     */
    @GetMapping("/hw390/test")
    public ResponseEntity<Map<String, Object>> testHw390Configuration() {
        return sendHw390Configuration(15.0f, 85.0f);
    }
}

