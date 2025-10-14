package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.entities.AlertSettings;
import com.laboratorio.iot.plantix.repositories.IAlertSettingsRepository;
import com.laboratorio.iot.plantix.services.IAlertSettingsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlertSettingsServiceImpl implements IAlertSettingsService {
    private final IAlertSettingsRepository repo;

    @Value("${plantix.alerts.default-recipients:admin@gmail.com}")
    private String defaultRecipients;

    @Value("${plantix.alerts.min-interval-minutes:5}")
    private int defaultMinInterval;

    @Value("${plantix.alerts.valve.open-timeout-minutes:10}")
    private int defaultValveTimeout;

    public AlertSettingsServiceImpl(IAlertSettingsRepository repo) {
        this.repo = repo;
    }

    @Override
    public AlertSettings getOrDefaults() {
        return repo.findAll().stream().findFirst().orElseGet(() -> {
            AlertSettings s = new AlertSettings();
            s.setRecipients(defaultRecipients);
            s.setEnableRangeAlerts(true);
            s.setEnableValveAlerts(true);
            s.setEnableEsp32ErrorAlerts(true);
            s.setMinIntervalMinutes(defaultMinInterval);
            s.setValveOpenTimeoutMinutes(defaultValveTimeout);
            return repo.save(s);
        });
    }

    @Override
    public AlertSettings save(AlertSettings settings) {
        return repo.save(settings);
    }

    @Override
    public String[] getRecipientsArray() {
        String rec = getOrDefaults().getRecipients();
        if (rec == null || rec.isBlank()) rec = defaultRecipients;
        return rec.replace(";", ",").split(",");
    }

    @Override
    public int getMinIntervalMinutes() {
        Integer v = getOrDefaults().getMinIntervalMinutes();
        return v != null ? v : defaultMinInterval;
    }

    @Override
    public int getValveTimeoutMinutes() {
        Integer v = getOrDefaults().getValveOpenTimeoutMinutes();
        return v != null ? v : defaultValveTimeout;
    }
}

