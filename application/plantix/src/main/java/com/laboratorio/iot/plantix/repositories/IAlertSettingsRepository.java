package com.laboratorio.iot.plantix.repositories;

import com.laboratorio.iot.plantix.entities.AlertSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlertSettingsRepository extends JpaRepository<AlertSettings, Long> {
}

