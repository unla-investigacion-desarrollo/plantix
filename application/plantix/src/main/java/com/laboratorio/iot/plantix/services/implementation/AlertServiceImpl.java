package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.configuration.mqtt.hw390.out.MQTTHw390Gateway;
import com.laboratorio.iot.plantix.dtos.mqtt.HW390ConfigurationDTO;
import com.laboratorio.iot.plantix.entities.Field;
import com.laboratorio.iot.plantix.entities.SensorHistory;
import com.laboratorio.iot.plantix.repositories.IDeviceHistoryRepository;
import com.laboratorio.iot.plantix.services.IAlertService;
import com.laboratorio.iot.plantix.services.IAlertSettingsService;
import com.laboratorio.iot.plantix.services.IMailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlertServiceImpl implements IAlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final IMailService mailService;
    private final IAlertSettingsService settingsService;
    private final IDeviceHistoryRepository deviceHistoryRepository;
    private final MQTTHw390Gateway mqttHw390Gateway;
    private final ObjectMapper objectMapper;

    // memoria anti-spam por campo/métrica y por dispositivo
    private final Map<String, LocalDateTime> lastSent = new ConcurrentHashMap<>();

    public AlertServiceImpl(IMailService mailService, IAlertSettingsService settingsService,
                           IDeviceHistoryRepository deviceHistoryRepository,
                           MQTTHw390Gateway mqttHw390Gateway,
                           ObjectMapper objectMapper) {
        this.mailService = mailService;
        this.settingsService = settingsService;
        this.deviceHistoryRepository = deviceHistoryRepository;
        this.mqttHw390Gateway = mqttHw390Gateway;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processNewReading(SensorHistory savedHistory) {
        try {
            if (!settingsService.getOrDefaults().isEnableRangeAlerts()) return;
            if (savedHistory == null || savedHistory.getSensor() == null) return;
            Field field = savedHistory.getSensor().getField();
            if (field == null) return;

            String sensorName = savedHistory.getSensor().getName();
            String[] recipients = settingsService.getRecipientsArray();

            // DHT11: data "temp,hum"
            if ("DHT11".equalsIgnoreCase(sensorName)) {
                String data = savedHistory.getData();
                if (data != null && data.contains(",")) {
                    String[] parts = data.split(",");
                    double temp = Double.parseDouble(parts[0]);
                    double hum = Double.parseDouble(parts[1]);
                    checkAndSend(field, "temperatura", temp, field.getTemperatureMin(), field.getTemperatureMax(), recipients);
                    checkAndSend(field, "humedad", hum, field.getHumidityMin(), field.getHumidityMax(), recipients);
                }
            }
            // HW390: data es solo humedad de sustrato
            if ("HW390".equalsIgnoreCase(sensorName)) {
                String data = savedHistory.getData();
                if (data != null && !data.isBlank()) {
                    double moist = Double.parseDouble(data);
                    checkAndSend(field, "humedad de sustrato", moist, field.getSubstrateHumidityMin(), field.getSubstrateHumidityMax(), recipients);
                }
            }
        } catch (Exception e) {
            logger.error("Error procesando alertas de lectura: {}", e.getMessage(), e);
        }
    }

    private void checkAndSend(Field field, String metric, double value, Double min, Double max, String[] recipients) {
        if (min == null && max == null) return;
        boolean out = (min != null && value < min) || (max != null && value > max);
        if (!out) return;

        int gap = settingsService.getMinIntervalMinutes();
        String key = field.getId() + ":" + metric;
        LocalDateTime last = lastSent.get(key);
        if (last != null && Duration.between(last, LocalDateTime.now()).toMinutes() < gap) return;

        mailService.sendRangeAlert(recipients, field.getLocation(), metric,
                String.format("%.2f", value),
                min != null ? String.format("%.2f", min) : "-",
                max != null ? String.format("%.2f", max) : "-");
        lastSent.put(key, LocalDateTime.now());
    }

    // chequeo cada minuto electroválvulas abiertas sin cierre
    @Scheduled(fixedDelayString = "60000")
    public void checkValvesOpen() {
        try {
            if (!settingsService.getOrDefaults().isEnableValveAlerts()) return;
            int timeout = settingsService.getValveTimeoutMinutes();
            var openHistories = deviceHistoryRepository.findAllByPowerOffTimeIsNull();
            String[] recipients = settingsService.getRecipientsArray();
            LocalDateTime now = LocalDateTime.now();
            openHistories.forEach(h -> {
                long minutes = Duration.between(h.getPowerOnTime(), now).toMinutes();
                if (minutes >= timeout) {
                    String key = "valve:" + h.getDevice().getId();
                    LocalDateTime last = lastSent.get(key);
                    if (last != null && Duration.between(last, now).toMinutes() < settingsService.getMinIntervalMinutes()) return;
                    String loc = h.getDevice().getField() != null ? h.getDevice().getField().getLocation() : "(desconocido)";
                    mailService.sendValveOpenAlert(recipients, loc, h.getDevice().getId(), minutes);
                    lastSent.put(key, now);
                }
            });
        } catch (Exception e) {
            logger.error("Error verificando electroválvulas: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendTestEmail(String[] recipients) {
        try {
            logger.info("Enviando correo de prueba a: {}", String.join(", ", recipients));
            mailService.sendRangeAlert(
                recipients,
                "Campo de Prueba",
                "temperatura",
                "35.5",
                "10.0",
                "30.0"
            );
            logger.info("Correo de prueba enviado exitosamente");
        } catch (Exception e) {
            logger.error("Error enviando correo de prueba: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar correo de prueba: " + e.getMessage());
        }
    }

    @Override
    public void sendHw390Configuration(Float minValue, Float maxValue) {
        try {
            logger.info("Enviando configuración HW390 - Min: {}, Max: {}", minValue, maxValue);

            HW390ConfigurationDTO configDTO = new HW390ConfigurationDTO(minValue, maxValue);
            String jsonPayload = objectMapper.writeValueAsString(configDTO);

            mqttHw390Gateway.sendToMqtt(jsonPayload);

            logger.info("Configuración HW390 enviada exitosamente al tópico MQTT");
        } catch (Exception e) {
            logger.error("Error enviando configuración HW390 por MQTT: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar configuración HW390: " + e.getMessage(), e);
        }
    }
}
