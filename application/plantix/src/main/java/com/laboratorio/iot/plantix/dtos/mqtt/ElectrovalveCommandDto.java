package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class ElectrovalveCommandDto {
    @NotNull(message = "El ID del field es obligatorio")
    private Long field;
    
    @NotNull(message = "El tiempo de apertura es obligatorio")
    @Min(value = 1, message = "El tiempo de apertura mínimo es 1 segundo")
    @Max(value = 3600, message = "El tiempo de apertura máximo es 3600 segundos (1 hora)")
    @JsonProperty("open_time")
    private Integer openTime;
    
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;
}
