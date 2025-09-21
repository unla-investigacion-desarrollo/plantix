package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.SensorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISensorHistoryRepository extends JpaRepository<SensorHistory, Long> {
    // Encontrar el último registro por sensor_id ordenado por timestamp descendente
    Optional<SensorHistory> findTopBySensorIdOrderByTimestampDesc(Long sensorId);

    List<SensorHistory> findTop10BySensorIdOrderByTimestampDesc(Long sensorId);
}
