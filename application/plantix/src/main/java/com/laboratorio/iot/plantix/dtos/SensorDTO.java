package com.laboratorio.iot.plantix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorDTO {
    private Long id;
    private String name;
    private Long fieldId;
    private Integer measurementInterval; // in seconds
    private String latestValue; // Add latestValue for view rendering
}
