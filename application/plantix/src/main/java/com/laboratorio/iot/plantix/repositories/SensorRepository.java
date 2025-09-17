package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findByFieldIdAndName(Long fieldId, String name);
    Optional<Sensor> findByExternalId(String externalId);
}
