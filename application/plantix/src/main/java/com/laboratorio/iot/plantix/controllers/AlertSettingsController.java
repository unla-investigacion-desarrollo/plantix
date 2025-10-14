package com.laboratorio.iot.plantix.controllers;

import com.laboratorio.iot.plantix.dtos.AlertSettingsDTO;
import com.laboratorio.iot.plantix.entities.AlertSettings;
import com.laboratorio.iot.plantix.services.IAlertService;
import com.laboratorio.iot.plantix.services.IAlertSettingsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/alerts")
public class AlertSettingsController {

    private final IAlertSettingsService alertSettingsService;
    private final IAlertService alertService;

    @Value("${mail.username:}")
    private String mailUsername;

    public AlertSettingsController(IAlertSettingsService alertSettingsService, IAlertService alertService) {
        this.alertSettingsService = alertSettingsService;
        this.alertService = alertService;
    }

    @GetMapping("/settings")
    public String showAlertSettings(Model model) {
        // Obtener configuración actual desde la BD (o valores por defecto)
        AlertSettings currentSettings = alertSettingsService.getOrDefaults();

        // Convertir entidad a DTO para la vista
        AlertSettingsDTO dto = new AlertSettingsDTO();
        dto.setRecipients(currentSettings.getRecipients());
        dto.setEnableRangeAlerts(currentSettings.isEnableRangeAlerts());
        dto.setEnableValveAlerts(currentSettings.isEnableValveAlerts());
        dto.setEnableEsp32ErrorAlerts(currentSettings.isEnableEsp32ErrorAlerts());
        dto.setMinIntervalMinutes(currentSettings.getMinIntervalMinutes());
        dto.setValveOpenTimeoutMinutes(currentSettings.getValveOpenTimeoutMinutes());

        model.addAttribute("settings", dto);
        model.addAttribute("mailUsername", mailUsername);

        return "alerts/settings";
    }

    @GetMapping
    public String redirectToSettings() {
        return "redirect:/admin/alerts/settings";
    }

    @PostMapping
    public String updateAlertSettings(
            @ModelAttribute("settings") AlertSettingsDTO dto,
            RedirectAttributes redirectAttributes) {

        // Obtener configuración existente o crear nueva
        AlertSettings settings = alertSettingsService.getOrDefaults();

        // Actualizar con los valores del formulario
        settings.setRecipients(dto.getRecipients());
        settings.setEnableRangeAlerts(dto.getEnableRangeAlerts() != null ? dto.getEnableRangeAlerts() : false);
        settings.setEnableValveAlerts(dto.getEnableValveAlerts() != null ? dto.getEnableValveAlerts() : false);
        settings.setEnableEsp32ErrorAlerts(dto.getEnableEsp32ErrorAlerts() != null ? dto.getEnableEsp32ErrorAlerts() : false);
        settings.setMinIntervalMinutes(dto.getMinIntervalMinutes());
        settings.setValveOpenTimeoutMinutes(dto.getValveOpenTimeoutMinutes());

        // Guardar en la base de datos
        alertSettingsService.save(settings);

        redirectAttributes.addFlashAttribute("saved", true);

        return "redirect:/admin/alerts/settings";
    }

    @PostMapping("/test")
    public String sendTestEmail(RedirectAttributes redirectAttributes) {
        try {
            String[] recipients = alertSettingsService.getRecipientsArray();

            if (recipients == null || recipients.length == 0) {
                redirectAttributes.addFlashAttribute("error", "No hay destinatarios configurados");
                return "redirect:/admin/alerts/settings";
            }

            alertService.sendTestEmail(recipients);
            redirectAttributes.addFlashAttribute("testSent", true);
            redirectAttributes.addFlashAttribute("testMessage",
                "Correo de prueba enviado a: " + String.join(", ", recipients));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "Error al enviar correo de prueba: " + e.getMessage());
        }

        return "redirect:/admin/alerts/settings";
    }
}

