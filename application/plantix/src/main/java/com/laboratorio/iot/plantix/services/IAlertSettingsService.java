package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.entities.AlertSettings;

public interface IAlertSettingsService {
    AlertSettings getOrDefaults();
    AlertSettings save(AlertSettings settings);
    String[] getRecipientsArray();
    int getMinIntervalMinutes();
    int getValveTimeoutMinutes();
}

