package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "plant_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double phValue;
    private Double substrateHumidity;
    private Double temperature;
    private Double light;

    @ManyToMany(mappedBy = "plantTypes")
    private List<Field> fields;


}
