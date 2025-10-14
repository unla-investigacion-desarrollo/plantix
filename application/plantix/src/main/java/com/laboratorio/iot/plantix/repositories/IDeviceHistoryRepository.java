package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.DeviceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDeviceHistoryRepository extends JpaRepository<DeviceHistory, Long> {
    Optional<DeviceHistory> findTopByDeviceIdOrderByPowerOnTimeDesc(Long deviceId);
    Optional<DeviceHistory> findTopByDeviceIdAndPowerOffTimeIsNullOrderByPowerOnTimeDesc(Long deviceId);
    List<DeviceHistory> findAllByPowerOffTimeIsNull();
}
