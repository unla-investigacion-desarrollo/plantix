package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Device;
import com.laboratorio.iot.plantix.entities.DeviceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceHistoryRepository extends JpaRepository<DeviceHistory, Long> {
    Optional<DeviceHistory> findFirstByDeviceOrderByPowerOnTimeDesc(Device device);
    Optional<DeviceHistory> findTopByDeviceOrderByEventTimeDesc(Device device);
}
