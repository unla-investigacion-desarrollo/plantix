package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.services.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService implements IMailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendTemplate(String to, String subject, String template, Map<String, Object> variables) {
        sendTemplate(new String[]{to}, subject, template, variables);
    }

    @Override
    public void sendTemplate(String[] to, String subject, String template, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            String html = templateEngine.process(template, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setFrom("NoReply Plantix <" + fromEmail + ">");
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Error enviando email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendRangeAlert(String[] to, String fieldLocation, String metric, String value, String min, String max) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fieldLocation", fieldLocation);
        vars.put("metric", metric);
        vars.put("value", value);
        vars.put("min", min);
        vars.put("max", max);
        sendTemplate(to, "[Plantix] Alerta de rango: " + metric, "mail/range-alert", vars);
    }

    @Override
    public void sendValveOpenAlert(String[] to, String fieldLocation, long deviceId, long minutesOpen) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fieldLocation", fieldLocation);
        vars.put("deviceId", deviceId);
        vars.put("minutesOpen", minutesOpen);
        sendTemplate(to, "[Plantix] Electroválvula abierta demasiado tiempo", "mail/valve-open-alert", vars);
    }

    @Override
    public void sendEsp32ErrorAlert(String[] to, String topic, String errorMessage) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("topic", topic);
        vars.put("errorMessage", errorMessage);
        sendTemplate(to, "[Plantix] Error de comunicación con ESP32", "mail/esp32-error-alert", vars);
    }
}
