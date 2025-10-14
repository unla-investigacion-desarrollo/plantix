package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alert_settings")
@Getter
@Setter
@NoArgsConstructor
public class AlertSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CSV de emails
    @Column(length = 1000)
    private String recipients;

    private boolean enableRangeAlerts = true;
    private boolean enableValveAlerts = true;
    private boolean enableEsp32ErrorAlerts = true;

    private Integer minIntervalMinutes; // si es null usar propiedad
    private Integer valveOpenTimeoutMinutes; // si es null usar propiedad
}

