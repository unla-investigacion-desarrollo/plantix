package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @ManyToMany
    @JoinTable(
        name = "field_planttype",
        joinColumns = @JoinColumn(name = "field_id"),
        inverseJoinColumns = @JoinColumn(name = "planttype_id")
    )
    private List<PlantType> plantTypes;

    @OneToMany(mappedBy = "field", fetch = FetchType.LAZY)
    private List<Device> devices;

    @OneToMany(mappedBy = "field", fetch = FetchType.LAZY)
    private List<Sensor> sensors;

    private Double phValue;
    private Double phValueMin;
    private Double phValueMax;

    private Double substrateHumidity;
    private Double substrateHumidityMin;
    private Double substrateHumidityMax;

    private Double temperature;
    private Double temperatureMin;
    private Double temperatureMax;

    private Double humidity;
    private Double humidityMin;
    private Double humidityMax;

    private Double light;
    private Double lightMin;
    private Double lightMax;

    @Lob
    @Column(name = "notes")
    private String notes;
}
