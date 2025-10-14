package com.laboratorio.iot.plantix.configuration.mqtt.electrovalve.in;

import com.laboratorio.iot.plantix.configuration.mqtt.MQTTPayloadMapper;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.dtos.mqtt.electrovalve.ElectrovalveInputDTO;
import com.laboratorio.iot.plantix.entities.Device;
import com.laboratorio.iot.plantix.entities.DeviceHistory;
import com.laboratorio.iot.plantix.repositories.IDeviceHistoryRepository;
import com.laboratorio.iot.plantix.repositories.IDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.time.LocalDateTime;

@Configuration
public class MQTTElectrovalveInputConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MQTTElectrovalveInputConfiguration.class);
    private final MQTTPayloadMapper payloadMapper;
    private final IDeviceRepository deviceRepository;
    private final IDeviceHistoryRepository deviceHistoryRepository;

    public MQTTElectrovalveInputConfiguration(MQTTPayloadMapper payloadMapper, IDeviceRepository deviceRepository, IDeviceHistoryRepository deviceHistoryRepository) {
        this.payloadMapper = payloadMapper;
        this.deviceRepository = deviceRepository;
        this.deviceHistoryRepository = deviceHistoryRepository;
    }

    @Bean
    public MessageChannel electrovalveOpenInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel electrovalveCloseInputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.ELECTROVALVE_OPEN_CHANNEL)
    public void electrovalveOpenHandleMessage(Message<?> message) {
        String json = message.getPayload().toString();
        try {
            ElectrovalveInputDTO dto = payloadMapper.mapToThisClass(json, ElectrovalveInputDTO.class);
            Device device = deviceRepository.findById(dto.getDeviceId()).orElse(null);
            if (device == null) {
                logger.warn("Electroválvula no encontrada: {}", dto.getDeviceId());
                return;
            }
            DeviceHistory h = new DeviceHistory();
            h.setDevice(device);
            h.setPowerOnTime(LocalDateTime.now());
            h.setPowerOffTime(null);
            deviceHistoryRepository.save(h);
            logger.info("Electroválvula {} abierta (historial registrado)", device.getId());
        } catch (Exception e) {
            logger.error("Error procesando apertura de electroválvula: {}", e.getMessage(), e);
        }
    }

    @ServiceActivator(inputChannel = MQTTInputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL)
    public void electrovalveCloseHandleMessage(Message<?> message) {
        String json = message.getPayload().toString();
        try {
            ElectrovalveInputDTO dto = payloadMapper.mapToThisClass(json, ElectrovalveInputDTO.class);
            var historyOpt = deviceHistoryRepository.findTopByDeviceIdAndPowerOffTimeIsNullOrderByPowerOnTimeDesc(dto.getDeviceId());
            if (historyOpt.isEmpty()) {
                logger.warn("No se encontró historial abierto para cerrar. deviceId={}", dto.getDeviceId());
                return;
            }
            DeviceHistory h = historyOpt.get();
            h.setPowerOffTime(LocalDateTime.now());
            deviceHistoryRepository.save(h);
            logger.info("Electroválvula {} cerrada (historial actualizado)", dto.getDeviceId());
        } catch (Exception e) {
            logger.error("Error procesando cierre de electroválvula: {}", e.getMessage(), e);
        }
    }
}
