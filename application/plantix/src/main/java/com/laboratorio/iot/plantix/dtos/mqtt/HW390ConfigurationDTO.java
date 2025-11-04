package com.laboratorio.iot.plantix.dtos.mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HW390ConfigurationDTO {

    private Float minValue;
    private Float maxValue;
}

