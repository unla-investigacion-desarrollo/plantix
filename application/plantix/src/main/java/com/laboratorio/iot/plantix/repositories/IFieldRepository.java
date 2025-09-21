package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFieldRepository extends JpaRepository<Field, Long> {
    Field findById(long fieldId);
}
