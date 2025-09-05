package com.laboratorio.iot.plantix.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class ErrorMessageDto {
    @NotEmpty(message = "El ID de la solicitud es obligatorio")
    @JsonProperty("request_id")
    private String requestId;
    
    @NotEmpty(message = "El código de error es obligatorio")
    @JsonProperty("error_code")
    private String errorCode;
    
    @NotEmpty(message = "El mensaje de error es obligatorio")
    private String message;
    
    @NotNull(message = "La marca de tiempo es obligatoria")
    private Instant timestamp;
}
