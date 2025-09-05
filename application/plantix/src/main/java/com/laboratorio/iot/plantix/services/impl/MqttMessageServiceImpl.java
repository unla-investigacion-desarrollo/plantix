package com.laboratorio.iot.plantix.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laboratorio.iot.plantix.dtos.mqtt.*;
import com.laboratorio.iot.plantix.entities.Device;
import com.laboratorio.iot.plantix.entities.DeviceHistory;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.repositories.DeviceHistoryRepository;
import com.laboratorio.iot.plantix.repositories.DeviceRepository;
import com.laboratorio.iot.plantix.repositories.SensorRepository;
import com.laboratorio.iot.plantix.services.MqttMessageService;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laboratorio.iot.plantix.dtos.mqtt.*;
import com.laboratorio.iot.plantix.entities.Device;
import com.laboratorio.iot.plantix.entities.DeviceHistory;
import com.laboratorio.iot.plantix.entities.Sensor;
import com.laboratorio.iot.plantix.exceptions.mqtt.MQTTInvalidPayloadException;
import com.laboratorio.iot.plantix.repositories.DeviceHistoryRepository;
import com.laboratorio.iot.plantix.repositories.DeviceRepository;
import com.laboratorio.iot.plantix.repositories.SensorRepository;
import com.laboratorio.iot.plantix.services.MqttMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class MqttMessageServiceImpl implements MqttMessageService {
    
    private final ObjectMapper objectMapper;
    private final DeviceRepository deviceRepository;
    private final DeviceHistoryRepository deviceHistoryRepository;
    private final SensorRepository sensorRepository;
    
    @Autowired
    public MqttMessageServiceImpl(ObjectMapper objectMapper,
                                DeviceRepository deviceRepository,
                                DeviceHistoryRepository deviceHistoryRepository,
                                SensorRepository sensorRepository) {
        this.objectMapper = objectMapper;
        this.deviceRepository = deviceRepository;
        this.deviceHistoryRepository = deviceHistoryRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        String payload = message.getPayload().toString();
        
        try {
            if (topic.endsWith("/sensor/dht11")) {
                processDht11Data(payload);
            } else if (topic.endsWith("/sensor/substrate_moisture")) {
                processSubstrateMoistureData(payload);
            } else if (topic.endsWith("/electrovalve/open")) {
                processElectrovalveOpenCommand(payload);
            } else if (topic.endsWith("/electrovalve/close")) {
                processElectrovalveCloseConfirmation(payload);
            } else if (topic.endsWith("/errors")) {
                processErrorMessage(payload);
            } else if (topic.endsWith("/request/sensor/data")) {
                processSensorDataRequest(payload);
            } else {
                log.warn("Tópico MQTT no reconocido: {}", topic);
            }
        } catch (MQTTInvalidPayloadException | JsonProcessingException e) {
            log.error("Error al procesar mensaje MQTT en tópico {}: {}", topic, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar mensaje MQTT en tópico {}: {}", topic, e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void processSensorDataRequest(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            // Parsear el JSON de la solicitud
            SensorDataRequestDto request = objectMapper.readValue(payload, SensorDataRequestDto.class);
            log.info("Solicitud de datos de sensores recibida - ID: {}, Marca de tiempo: {}", 
                    request.getRequestId(), request.getTimestamp());
            
            // Validar que la solicitud contenga al menos un grupo de sensores
            if (request.getSensorGroups() == null || request.getSensorGroups().isEmpty()) {
                throw new MQTTInvalidPayloadException("La solicitud debe contener al menos un grupo de sensores");
            }
            
            // Procesar cada grupo de sensores en la solicitud
            for (SensorDataRequestDto.SensorGroupDto sensorGroup : request.getSensorGroups()) {
                procesarGrupoSensores(request, sensorGroup);
            }
            
            log.debug("Procesamiento de solicitud de datos completado - ID: {}", request.getRequestId());
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar la solicitud de datos de sensores: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Formato de solicitud inválido", e);
        } catch (MQTTInvalidPayloadException e) {
            log.error("Solicitud de datos de sensores inválida: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar la solicitud de datos de sensores: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error al procesar la solicitud de datos de sensores", e);
        }
    }
    
    /**
     * Procesa un único grupo de sensores de la solicitud
     */
    private void processSensorGroup(SensorDataRequestDto request, SensorDataRequestDto.SensorGroupDto sensorGroup) 
            throws MQTTInvalidPayloadException {
        String sensorType = sensorGroup.getType().toUpperCase();
        log.debug("Procesando grupo de sensores - Tipo: {}, Campos: {}", 
                sensorType, sensorGroup.getFieldIds());
        
        // Manejar diferentes tipos de sensores
        switch (sensorType) {
            case "DHT11":
                handleDht11Request(request, sensorGroup);
                break;
            case "SUBSOIL_MOISTURE":
                handleSubsoilMoistureRequest(request, sensorGroup);
                break;
            default:
                log.warn("Tipo de sensor no soportado: {}", sensorType);
                throw new MQTTInvalidPayloadException(
                    String.format("Tipo de sensor no soportado: %s", sensorType));
        }
    }
    
    /**
     * Maneja las solicitudes de datos del sensor DHT11
     */
    private void handleDht11Request(SensorDataRequestDto request, 
                                   SensorDataRequestDto.SensorGroupDto sensorGroup) {
        if (sensorGroup.getFieldIds() == null || sensorGroup.getFieldIds().isEmpty()) {
            // Solicitud para todos los sensores DHT11
            log.info("Solicitando datos de todos los sensores DHT11");
            // Publicar en el tópico general de solicitud DHT11
            String topic = String.format("plantix/request/sensor/dht11");
            publishSensorRequest(topic, request.getRequestId());
        } else {
            // Solicitud para campos específicos
            for (Long fieldId : sensorGroup.getFieldIds()) {
                log.info("Solicitando datos del sensor DHT11 para el field: {}", fieldId);
                String topic = String.format("plantix/field/%d/sensor/dht11/request", fieldId);
                publishSensorRequest(topic, request.getRequestId());
            }
        }
    }
    
    /**
     * Maneja las solicitudes de datos de sensores de humedad del suelo
     */
    private void handleSubsoilMoistureRequest(SensorDataRequestDto request, 
                                             SensorDataRequestDto.SensorGroupDto sensorGroup) {
        if (sensorGroup.getFieldIds() == null || sensorGroup.getFieldIds().isEmpty()) {
            // Solicitud para todos los sensores de humedad
            log.info("Solicitando datos de todos los sensores de humedad de sustrato");
            String topic = "plantix/request/sensor/substrate_moisture";
            publishSensorRequest(topic, request.getRequestId());
        } else {
            // Solicitud para campos específicos
            for (Long fieldId : sensorGroup.getFieldIds()) {
                log.info("Solicitando datos de humedad de sustrato para el field: {}", fieldId);
                String topic = String.format("plantix/field/%d/sensor/substrate_moisture/request", fieldId);
                publishSensorRequest(topic, request.getRequestId());
            }
        }
    }
    
    /**
     * Método auxiliar para publicar una solicitud de sensor en MQTT
     */
    private void publishSensorRequest(String topic, String requestId) {
        try {
            // Crear un objeto de solicitud simple con solo el ID de solicitud
            String payload = String.format("{\"request_id\":\"%s\"}", requestId);
            
            // En una implementación real, se usaría el cliente MQTT para publicar el mensaje
            // Por ejemplo: mqttClient.publish(topic, payload.getBytes(), 1, false);
            log.debug("Publicando solicitud - Tópico: {}, Datos: {}", topic, payload);
            
        } catch (Exception e) {
            log.error("Error al publicar la solicitud de datos en el tópico {}: {}", 
                    topic, e.getMessage(), e);
            // No lanzamos la excepción para permitir que continúen otras solicitudes
        }
    }
    }

    @Override
    @Transactional
    public void processDht11Data(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            Dht11DataDto dto = objectMapper.readValue(payload, Dht11DataDto.class);
            log.info("Datos DHT11 recibidos - Field: {}, Temperatura: {}°C, Humedad: {}%, Timestamp: {}", 
                    dto.getField(), dto.getTemperature(), dto.getHumidity(), dto.getTimestamp());
            
            // Validar que el fieldId exista
            if (dto.getField() == null) {
                throw new MQTTInvalidPayloadException("El campo 'field' es requerido");
            }
            
            // Buscar el dispositivo por field y tipo
            Optional<Device> deviceOpt = deviceRepository.findByFieldIdAndType(
                dto.getField().longValue(), 
                "DHT11"
            );
            
            if (deviceOpt.isEmpty()) {
                log.warn("No se encontró un dispositivo DHT11 para el field: {}", dto.getField());
                return;
            }
            
            Device device = deviceOpt.get();
            
            // Crear y guardar el registro de historial
            DeviceHistory history = new DeviceHistory();
            history.setDevice(device);
            history.setEventTime(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
            history.setTemperature(dto.getTemperature());
            history.setHumidity(dto.getHumidity());
            history.setEventType("DHT11_READING");
            
            deviceHistoryRepository.save(history);
            log.debug("Registro de DHT11 guardado exitosamente para el dispositivo ID: {}", device.getId());
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar datos DHT11: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Error al procesar datos DHT11", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar datos DHT11: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error inesperado al procesar datos DHT11", e);
        }
    }

    @Override
    @Transactional
    public void processSubstrateMoistureData(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            SubstrateMoistureDataDto dto = objectMapper.readValue(payload, SubstrateMoistureDataDto.class);
            log.info("Datos de humedad de sustrato recibidos - Field: {}, Sensor: {}, Humedad: {}%, Timestamp: {}", 
                    dto.getField(), dto.getSensorId(), dto.getMoisture(), dto.getTimestamp());
            
            // Validaciones
            if (dto.getField() == null) {
                throw new MQTTInvalidPayloadException("El campo 'field' es requerido");
            }
            if (dto.getSensorId() == null || dto.getSensorId().trim().isEmpty()) {
                throw new MQTTInvalidPayloadException("El ID del sensor es requerido");
            }
            
            // Buscar el sensor por su ID
            Optional<Sensor> sensorOpt = sensorRepository.findByExternalId(dto.getSensorId());
            
            if (sensorOpt.isEmpty()) {
                log.warn("No se encontró el sensor con ID: {}", dto.getSensorId());
                return;
            }
            
            Sensor sensor = sensorOpt.get();
            
            // Actualizar el último valor del sensor
            sensor.setLastMoistureReading(dto.getMoisture());
            sensor.setLastReadingTime(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
            
            // Guardar el registro de historial
            DeviceHistory history = new DeviceHistory();
            history.setDevice(sensor.getDevice());
            history.setEventTime(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
            history.setMoisture(dto.getMoisture());
            history.setEventType("MOISTURE_READING");
            
            sensorRepository.save(sensor);
            deviceHistoryRepository.save(history);
            
            log.debug("Lectura de humedad de sustrato guardada exitosamente para el sensor ID: {}", sensor.getId());
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar los datos de humedad del sustrato: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Formato de mensaje inválido para datos de humedad del sustrato", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar datos de humedad del sustrato: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error al procesar los datos de humedad del sustrato", e);
        }
    }

    /**
     * Valida los datos de un comando de electroválvula
     * @param dto DTO con los datos del comando
     * @throws MQTTInvalidPayloadException Si la validación falla
     */
    private void validateElectrovalveCommand(ElectrovalveCommandDto dto) throws MQTTInvalidPayloadException {
        if (dto.getFieldId() == null) {
            throw new MQTTInvalidPayloadException("El ID del campo es requerido");
        }
        if (dto.getValveId() == null || dto.getValveId().trim().isEmpty()) {
            throw new MQTTInvalidPayloadException("El ID de la válvula es requerido");
        }
        if (dto.getDurationSeconds() == null || dto.getDurationSeconds() <= 0) {
            throw new MQTTInvalidPayloadException("La duración debe ser mayor a 0 segundos");
        }
        if (dto.getDurationSeconds() > 3600) {
            throw new MQTTInvalidPayloadException("La duración máxima permitida es de 3600 segundos (1 hora)");
        }
    }

    @Override
    @Transactional
    public void processElectrovalveOpenCommand(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            // 1. Deserializar el mensaje
            ElectrovalveCommandDto dto = objectMapper.readValue(payload, ElectrovalveCommandDto.class);
            log.info("Comando de apertura de electroválvula recibido - Field: {}, Válvula: {}", 
                    dto.getFieldId(), dto.getValveId());
            
            // 2. Validar los datos del comando
            validateElectrovalveCommand(dto);
            
            // 3. Buscar el dispositivo de electroválvula
            Optional<Device> deviceOpt = deviceRepository.findByFieldIdAndTypeAndExternalId(
                dto.getFieldId().longValue(), 
                "ELECTROVALVE",
                dto.getValveId()
            );
            
            if (deviceOpt.isEmpty()) {
                String mensajeError = String.format("No se encontró la electroválvula con ID %s en el field %d", 
                    dto.getValveId(), dto.getFieldId());
                log.warn(mensajeError);
                throw new MQTTInvalidPayloadException(mensajeError);
            }
            
            Device device = deviceOpt.get();
            
            // 4. Verificar si la electroválvula ya está abierta
            Optional<DeviceHistory> ultimoEstadoOpt = deviceHistoryRepository.findTopByDeviceOrderByEventTimeDesc(device);
            if (ultimoEstadoOpt.isPresent() && "VALVE_OPEN_COMMAND".equals(ultimoEstadoOpt.get().getEventType())) {
                log.warn("La electroválvula {} ya se encuentra abierta", dto.getValveId());
                // Podrías decidir si lanzar una excepción o continuar dependiendo de tu lógica de negocio
            }
            
            // 5. Registrar el evento de apertura
            DeviceHistory history = new DeviceHistory();
            history.setDevice(device);
            history.setEventTime(LocalDateTime.now());
            history.setEventType("VALVE_OPEN_COMMAND");
            history.setDetails(String.format("Válvula abierta por comando. Duración: %d segundos", dto.getDurationSeconds()));
            
            // 6. Actualizar el estado del dispositivo
            device.setLastStatus("ABIERTA");
            device.setLastStatusUpdate(LocalDateTime.now());
            
            // 7. Guardar los cambios en la base de datos
            deviceRepository.save(device);
            deviceHistoryRepository.save(history);
            
            log.info("Comando de apertura de electroválvula registrado - Dispositivo ID: {}, Duración: {}s", 
                    device.getId(), dto.getDurationSeconds());
            
            // 8. Enviar comando MQTT a la electroválvula
            try {
                String topic = String.format("plantix/field/%d/electrovalve/%s/open", 
                    dto.getFieldId(), dto.getValveId());
                
                // Crear payload con la duración
                String mqttPayload = String.format("{\"duration_seconds\":%d}", dto.getDurationSeconds());
                
                // Enviar comando MQTT
                // mqttGateway.sendToMqtt(topic, mqttPayload);
                log.debug("Comando MQTT enviado - Tópico: {}, Payload: {}", topic, mqttPayload);
                
            } catch (Exception e) {
                log.error("Error al enviar comando MQTT a la electroválvula: {}", e.getMessage(), e);
                // No lanzamos la excepción para no fallar el proceso completo
            }
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar el comando de apertura de electroválvula: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Formato de mensaje inválido para comando de electroválvula", e);
        } catch (MQTTInvalidPayloadException e) {
            // Relanzamos la excepción para manejarla en el método que llama
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar comando de apertura de electroválvula: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error al procesar el comando de apertura de electroválvula", e);
        }
    }

    @Override
    @Transactional
    public void processElectrovalveCloseConfirmation(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            ElectrovalveCloseConfirmationDto dto = objectMapper.readValue(payload, ElectrovalveCloseConfirmationDto.class);
            log.info("Confirmación de cierre de electroválvula recibida - Field: {}, Válvula: {}", 
                    dto.getFieldId(), dto.getValveId());
            
            if (dto.getFieldId() == null) {
                throw new MQTTInvalidPayloadException("El campo 'fieldId' es requerido");
            }
            
            // Buscar el dispositivo de electroválvula por field y tipo
            Optional<Device> deviceOpt = deviceRepository.findByFieldIdAndType(
                dto.getFieldId().longValue(), 
                "ELECTROVALVE"
            );
            
            if (deviceOpt.isEmpty()) {
                log.warn("No se encontró una electroválvula para el field: {}", dto.getFieldId());
                return;
            }
            
            Device device = deviceOpt.get();
            
            // Registrar la confirmación de cierre
            DeviceHistory history = new DeviceHistory();
            history.setDevice(device);
            history.setEventTime(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
            history.setEventType("VALVE_CLOSE_CONFIRMATION");
            history.setDetails("Válvula cerrada confirmada por el dispositivo");
            
            if (dto.getWaterConsumedLiters() != null) {
                history.setWaterConsumedLiters(dto.getWaterConsumedLiters());
            }
            
            deviceHistoryRepository.save(history);
            log.info("Confirmación de cierre de electroválvula registrada para el dispositivo ID: {}", device.getId());
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar la confirmación de cierre de electroválvula: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Formato de mensaje inválido para confirmación de cierre de electroválvula", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar confirmación de cierre de electroválvula: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error al procesar la confirmación de cierre de electroválvula", e);
        }
    }

    @Override
    @Transactional
    public void processErrorMessage(String payload) throws MQTTInvalidPayloadException, JsonProcessingException {
        try {
            ErrorMessageDto dto = objectMapper.readValue(payload, ErrorMessageDto.class);
            log.error("Mensaje de error recibido - Código: {}, Mensaje: {}", dto.getErrorCode(), dto.getMessage());
            
            // Validaciones
            if (dto.getErrorCode() == null) {
                throw new MQTTInvalidPayloadException("El campo 'errorCode' es requerido");
            }
            
            // Registrar el error en el historial
            if (dto.getDeviceId() != null) {
                Optional<Device> deviceOpt = deviceRepository.findById(dto.getDeviceId());
                if (deviceOpt.isPresent()) {
                    DeviceHistory history = new DeviceHistory();
                    history.setDevice(deviceOpt.get());
                    history.setEventTime(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
                    history.setEventType("DEVICE_ERROR");
                    history.setDetails(String.format("Código: %s - %s", dto.getErrorCode(), dto.getMessage()));
                    
                    deviceHistoryRepository.save(history);
                    log.warn("Error registrado para el dispositivo ID: {}", dto.getDeviceId());
                } else {
                    log.warn("No se encontró el dispositivo con ID: {}", dto.getDeviceId());
                }
            } else {
                // Error general del sistema
                log.error("Error del sistema - Código: {}, Mensaje: {}", dto.getErrorCode(), dto.getMessage());
            }
            
            // Aca se podria agregar lógica adicional como notificaciones, alarmas, etc.
            
        } catch (JsonProcessingException e) {
            log.error("Error al procesar el mensaje de error: {}", e.getMessage());
            throw new MQTTInvalidPayloadException("Formato de mensaje de error inválido", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar mensaje de error: {}", e.getMessage(), e);
            throw new MQTTInvalidPayloadException("Error al procesar el mensaje de error", e);
        }
    }
    
    
}
