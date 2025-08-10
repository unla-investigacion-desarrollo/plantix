package com.laboratorio.iot.plantix.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorHistory {
    private Long id;
    private Sensor sensor;
    private LocalDateTime timestamp;
    private List<String> data;
}
