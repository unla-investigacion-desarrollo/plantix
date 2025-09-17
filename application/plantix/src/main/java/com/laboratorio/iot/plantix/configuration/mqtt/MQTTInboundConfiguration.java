package com.laboratorio.iot.plantix.configuration.mqtt;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.services.MqttMessageService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;

/**
 * Configuracion para mensajes MQTT entrantes (suscripción).
 */
@Configuration
public class MQTTInboundConfiguration {
    private static final String CLIENT_ID = "plantix-server";
    private static final int COMPLETION_TIMEOUT = 5000;
    private static final int QOS = MQTTBrokerInformation.DEFAULT_SENSOR_QOS;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        CLIENT_ID,
                        mqttClientFactory(),
                        MQTTBrokerInformation.REQUEST_SENSOR_DATA_TOPIC,
                        MQTTBrokerInformation.DHT11_TOPIC,
                        MQTTBrokerInformation.SUBSTRATE_MOISTURE_TOPIC,
                        MQTTBrokerInformation.ELECTROVALVE_OPEN_TOPIC,
                        MQTTBrokerInformation.ELECTROVALVE_CLOSE_TOPIC,
                        MQTTBrokerInformation.ERRORS_TOPIC
                );

        adapter.setCompletionTimeout(COMPLETION_TIMEOUT);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(QOS);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { MQTTBrokerInformation.URL });
        if (MQTTBrokerInformation.USERNAME != null) {
            options.setUserName(MQTTBrokerInformation.USERNAME);
        }
        if (MQTTBrokerInformation.PASSWORD != null) {
            options.setPassword(MQTTBrokerInformation.PASSWORD.toCharArray());
        }
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        return options;
    }

    // Router: dirige según el tópico recibido hacia canales dedicados
    @Router(inputChannel = "mqttInputChannel")
    public String mqttInboundRouter(org.springframework.messaging.Message<?> message) {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        if (topic == null) return null;
        if (topic.endsWith("/sensor/dht11")) return "dht11InputChannel";
        if (topic.endsWith("/sensor/substrate_moisture")) return "substrateMoistureInputChannel";
        if (topic.endsWith("/electrovalve/open")) return "electrovalveOpenInputChannel";
        if (topic.endsWith("/electrovalve/close")) return "electrovalveCloseInputChannel";
        if (topic.endsWith("/errors")) return "errorsInputChannel";
        if (MQTTBrokerInformation.REQUEST_SENSOR_DATA_TOPIC.equals(topic)) return "requestSensorDataInputChannel";
        return null; // sin canal si no coincide
    }

    // Canales dedicados
    @Bean
    public MessageChannel dht11InputChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel substrateMoistureInputChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel electrovalveOpenInputChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel electrovalveCloseInputChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel errorsInputChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel requestSensorDataInputChannel() { return new DirectChannel(); }

    // Handlers específicos por canal
    @Bean
    @ServiceActivator(inputChannel = "dht11InputChannel")
    public MessageHandler dht11Handler(MqttMessageService service) {
        return message -> {
            try {
                service.processDht11Data(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing DHT11 message", e);
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "substrateMoistureInputChannel")
    public MessageHandler substrateMoistureHandler(MqttMessageService service) {
        return message -> {
            try {
                service.processSubstrateMoistureData(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing substrate moisture message", e);
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "electrovalveOpenInputChannel")
    public MessageHandler electrovalveOpenHandler(MqttMessageService service) {
        return message -> {
            try {
                service.processElectrovalveOpenCommand(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing electrovalve open command", e);
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "electrovalveCloseInputChannel")
    public MessageHandler electrovalveCloseHandler(MqttMessageService service) {
        return message -> {
            try {
                service.processElectrovalveCloseConfirmation(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing electrovalve close confirmation", e);
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "errorsInputChannel")
    public MessageHandler errorsHandler(MqttMessageService service) {
        return message -> {
            try {
                service.processErrorMessage(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing error message", e);
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "requestSensorDataInputChannel")
    public MessageHandler requestSensorDataHandler(MqttMessageService service) {
        return message -> {
            try {
                service.processSensorDataRequest(message.getPayload().toString());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing sensor data request", e);
            }
        };
    }

    
}
