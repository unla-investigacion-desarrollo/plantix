package com.laboratorio.iot.plantix.configuration.mqtt.error.in;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.services.IAlertSettingsService;
import com.laboratorio.iot.plantix.services.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * Configuration for handling error messages received via MQTT.
 * Routes error messages to the appropriate error handling service.
 */
@Configuration
public class MQTTErrorInputConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MQTTErrorInputConfiguration.class);

    private final IMailService mailService;
    private final IAlertSettingsService settingsService;

    public MQTTErrorInputConfiguration(IMailService mailService, IAlertSettingsService settingsService) {
        this.mailService = mailService;
        this.settingsService = settingsService;
    }

    /**
     * Creates a channel for receiving error messages.
     * @return The error message channel
     */
    @Bean
    public MessageChannel errorInputChannel() {
        return new DirectChannel();
    }

    /**
     * Handles incoming error messages from MQTT.
     * @return A message handler for error messages
     */
    @Bean
    @ServiceActivator(inputChannel = MQTTInputChannelInformation.ERROR_CHANNEL)
    public org.springframework.messaging.MessageHandler errorMessageHandler() {
        return message -> {
            try {
                String errorMessage = message.getPayload().toString();
                String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

                logger.warn("Received error message from topic {}: {}", topic, errorMessage);

                if (settingsService.getOrDefaults().isEnableEsp32ErrorAlerts()) {
                    mailService.sendEsp32ErrorAlert(settingsService.getRecipientsArray(), topic, errorMessage);
                }
            } catch (Exception e) {
                logger.error("Error processing error message: {}", e.getMessage(), e);
            }
        };
    }
}
