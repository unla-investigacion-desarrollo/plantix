package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.dtos.SensorHistoryDTO;
import com.laboratorio.iot.plantix.repositories.ISensorHistoryRepository;
import com.laboratorio.iot.plantix.services.ISensorHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorHistoryService implements ISensorHistoryService {
    private final ISensorHistoryRepository sensorHistoryRepository;

    public SensorHistoryService(ISensorHistoryRepository sensorHistoryRepository) {
        this.sensorHistoryRepository = sensorHistoryRepository;
    }

    public SensorHistoryDTO getLastSensorHistoryBySensorId(Long sensorId) throws Exception {
        return sensorHistoryRepository.findTopBySensorIdOrderByTimestampDesc(sensorId)
                .map(sensorHistory -> new SensorHistoryDTO(
                        sensorHistory.getId(),
                        sensorHistory.getSensor().getId(),
                        sensorHistory.getTimestamp(),
                        sensorHistory.getData()
                ))
                .orElseThrow(()-> new Exception("No sensor history found for sensor ID: " + sensorId));
    }

    public List<SensorHistoryDTO> getLast10SensorHistoryBySensorId(Long sensorId) {
        return sensorHistoryRepository.findTop10BySensorIdOrderByTimestampDesc(sensorId)
                .stream()
                .map(sensorHistory -> new SensorHistoryDTO(
                        sensorHistory.getId(),
                        sensorHistory.getSensor().getId(),
                        sensorHistory.getTimestamp(),
                        sensorHistory.getData()
                )).toList();
    }

}
