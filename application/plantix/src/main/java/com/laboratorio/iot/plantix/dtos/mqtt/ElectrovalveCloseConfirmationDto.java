package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class ElectrovalveCloseConfirmationDto {
    @NotNull(message = "El ID del field es obligatorio")
    @JsonProperty("field")
    private Long fieldId;
    
    @NotEmpty(message = "El ID de la válvula es obligatorio")
    @JsonProperty("valve_id")
    private String valveId;
    
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;
    
    @JsonProperty("water_consumed_liters")
    private Double waterConsumedLiters;
}
