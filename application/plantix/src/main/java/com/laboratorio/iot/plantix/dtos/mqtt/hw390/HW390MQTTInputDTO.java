package com.laboratorio.iot.plantix.dtos.mqtt.hw390;

import com.laboratorio.iot.plantix.dtos.mqtt.MQTTInputDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HW390MQTTInputDTO extends MQTTInputDTO {
    private String data;
}