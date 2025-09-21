package com.laboratorio.iot.plantix.dtos;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO {
    private Long id;
    private String location;

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
    private String notes;
}
