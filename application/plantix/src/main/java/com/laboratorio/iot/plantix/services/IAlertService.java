package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.entities.SensorHistory;

public interface IAlertService {
    void processNewReading(SensorHistory savedHistory);
    void sendTestEmail(String[] recipients);
}
