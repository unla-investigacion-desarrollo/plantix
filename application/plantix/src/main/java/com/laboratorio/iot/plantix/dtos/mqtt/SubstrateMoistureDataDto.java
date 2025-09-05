package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class SubstrateMoistureDataDto {
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotNull(message = "El ID del field es obligatorio")
    private Long field;
    
    @NotEmpty(message = "El ID del sensor es obligatorio")
    @JsonProperty("sensor_id")
    private String sensorId;
    
    @NotNull(message = "El valor es obligatorio")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 1023, message = "El valor máximo es 1023")
    private Integer value;
    
    @NotNull(message = "El indicador raw es obligatorio")
    private Boolean raw;
    
    @NotNull(message = "El rango es obligatorio")
    private RangeDto range;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;

    @Data
    public static class RangeDto {
        @NotNull(message = "El valor seco es obligatorio")
        private Integer dry;
        
        @NotNull(message = "El valor húmedo es obligatorio")
        private Integer wet;
    }
}
