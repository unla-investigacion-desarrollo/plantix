package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Field;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Registered
public interface IFieldRepository extends JpaRepository<Field, Long> {
    Field findById(long fieldId);
}
