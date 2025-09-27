package com.laboratorio.iot.plantix.dtos.mqtt.dht11;

import com.laboratorio.iot.plantix.dtos.mqtt.MQTTInputDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DHT11MQTTInputDTO extends MQTTInputDTO {
    private DHT11Data data;
}
