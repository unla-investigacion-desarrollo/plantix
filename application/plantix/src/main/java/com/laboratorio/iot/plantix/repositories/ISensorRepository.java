package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findAllByFieldId(Long fieldId);
}
