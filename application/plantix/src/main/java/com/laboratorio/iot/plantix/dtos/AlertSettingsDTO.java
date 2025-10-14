package com.laboratorio.iot.plantix.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertSettingsDTO {
    private String recipients;
    private Boolean enableRangeAlerts = true;
    private Boolean enableValveAlerts = true;
    private Boolean enableEsp32ErrorAlerts = true;
    private Integer minIntervalMinutes = 30;
    private Integer valveOpenTimeoutMinutes = 60;
}

