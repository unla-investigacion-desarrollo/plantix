package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "power_on_time")
    private LocalDateTime powerOnTime;

    @Column(name = "power_off_time")
    private LocalDateTime powerOffTime;
}
