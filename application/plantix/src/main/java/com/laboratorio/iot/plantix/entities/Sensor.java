package com.laboratorio.iot.plantix.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    private long id;
    private String name;
    private List<SensorHistory> sensorHistories;
    private Field field;
}
