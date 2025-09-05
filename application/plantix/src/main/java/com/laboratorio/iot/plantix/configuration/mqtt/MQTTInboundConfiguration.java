package com.laboratorio.iot.plantix.configuration.mqtt;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.services.MqttMessageService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

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
    public MessageProducer inbound(MqttConnectOptions options) {
        MqttPahoMessageDrivenChannelAdapter adapter = 
                new MqttPahoMessageDrivenChannelAdapter(
                    MQTTBrokerInformation.URL, 
                    CLIENT_ID, 
                    mqttClientFactory(options));
        
        adapter.setCompletionTimeout(COMPLETION_TIMEOUT);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(QOS);
        adapter.setOutputChannel(mqttInputChannel());
        
        // Suscribirse a todos los tópicos requeridos
        adapter.addTopic(MQTTBrokerInformation.REQUEST_SENSOR_DATA_TOPIC);
        adapter.addTopic(MQTTBrokerInformation.DHT11_TOPIC);
        adapter.addTopic(MQTTBrokerInformation.SUBSTRATE_MOISTURE_TOPIC);
        adapter.addTopic(MQTTBrokerInformation.ELECTROVALVE_OPEN_TOPIC);
        adapter.addTopic(MQTTBrokerInformation.ELECTROVALVE_CLOSE_TOPIC);
        adapter.addTopic(MQTTBrokerInformation.ERRORS_TOPIC);
        
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler messageHandler(MqttMessageService mqttMessageService) {
        return mqttMessageService::handleMessage;
    }

    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory(MqttConnectOptions options) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
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
}
