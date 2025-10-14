package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeviceRepository extends JpaRepository<Device, Long> {
}

