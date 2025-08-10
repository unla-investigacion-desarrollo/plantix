package com.laboratorio.iot.plantix.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantType {
    private Long id;
    private String name;
    private double phValue;
    private double substrateHumidity;
    private double temperature;
    private double light;
    private List<Field> fields;


}
