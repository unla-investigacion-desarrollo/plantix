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
public class Field {
    private Long id;
    private String location;
    private List<PlantType> plantTypes;
    private List<Device> devices;
    private List<Sensor> sensors;
    private List<User> users;
}
