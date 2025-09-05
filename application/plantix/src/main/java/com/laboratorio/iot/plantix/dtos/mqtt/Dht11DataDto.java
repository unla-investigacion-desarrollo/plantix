package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class Dht11DataDto {
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotNull(message = "El ID del field es obligatorio")
    private Long field;
    
    @NotEmpty(message = "El ID del sensor es obligatorio")
    @JsonProperty("sensor_id")
    private String sensorId;
    
    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "-40.0", message = "La temperatura mínima es -40°C")
    @DecimalMax(value = "80.0", message = "La temperatura máxima es 80°C")
    private Double temperature;
    
    @NotNull(message = "La humedad es obligatoria")
    @DecimalMin(value = "0.0", message = "La humedad mínima es 0%")
    @DecimalMax(value = "100.0", message = "La humedad máxima es 100%")
    private Double humidity;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;
}
