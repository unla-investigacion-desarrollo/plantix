package com.laboratorio.iot.plantix.services;

import java.util.Map;

public interface IMailService {
    void sendTemplate(String to, String subject, String template, Map<String, Object> variables);
    void sendTemplate(String[] to, String subject, String template, Map<String, Object> variables);

    // Conveniencia para alertas
    void sendRangeAlert(String[] to, String fieldLocation, String metric, String value, String min, String max);
    void sendValveOpenAlert(String[] to, String fieldLocation, long deviceId, long minutesOpen);
    void sendEsp32ErrorAlert(String[] to, String topic, String errorMessage);
}
