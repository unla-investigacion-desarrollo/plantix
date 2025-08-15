package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sensor_histories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "sensor_history_data", joinColumns = @JoinColumn(name = "history_id"))
    @Column(name = "data_value")
    private List<String> data;
}
