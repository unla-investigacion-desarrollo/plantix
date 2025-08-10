package com.laboratorio.iot.plantix.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHistory {
    private Long id;
    private Device device;
    private LocalDateTime powerOnTime;
    private LocalDateTime powerOffTime;
}
