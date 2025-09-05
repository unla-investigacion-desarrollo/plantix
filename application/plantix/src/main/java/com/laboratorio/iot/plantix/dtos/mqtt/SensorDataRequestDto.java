package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SensorDataRequestDto {
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;
    
    @NotEmpty(message = "Debe especificar al menos un grupo de sensores")
    @JsonProperty("sensor_groups")
    private List<SensorGroupDto> sensorGroups;

    @Data
    public static class SensorGroupDto {
        @NotEmpty(message = "El tipo de sensor es obligatorio")
        private String type;
        
        @JsonProperty("fields")
        private List<Long> fieldIds;
        
        @JsonProperty("metrics")
        private List<String> metrics;
    }
}
