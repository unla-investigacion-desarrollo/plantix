package com.laboratorio.iot.plantix.dtos.mqtt.hw390;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HW390MQTTInputDTO extends MQTTInputDTO {
    private String data;
}