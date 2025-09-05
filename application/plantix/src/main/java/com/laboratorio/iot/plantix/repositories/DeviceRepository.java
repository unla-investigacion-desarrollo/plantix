package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByFieldIdAndType(Long fieldId, String type);
}
