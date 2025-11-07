package com.laboratorio.iot.plantix.dtos.mqtt.sensor;

import com.laboratorio.iot.plantix.dtos.mqtt.MQTTInputDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubstrateMoistureMQTTInputDTO extends MQTTInputDTO {
    private double value; // humedad de sustrato en %
}
