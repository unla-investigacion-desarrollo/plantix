package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Field;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface IFieldRepository extends JpaRepository<Field, Long> {
    Field findById(long fieldId);
}
