package com.laboratorio.iot.plantix.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorHistoryDTO {
    private Long id;
    private Long sensorId;
    private LocalDateTime timestamp;
    private String data;
}
