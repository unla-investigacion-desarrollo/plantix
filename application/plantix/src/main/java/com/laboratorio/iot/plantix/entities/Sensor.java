package com.laboratorio.iot.plantix.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "sensors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY)
    private List<SensorHistory> sensorHistories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;
}
