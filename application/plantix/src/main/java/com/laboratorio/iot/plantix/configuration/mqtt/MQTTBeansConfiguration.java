package com.laboratorio.iot.plantix.configuration.mqtt;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MQTTBeansConfiguration {
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(MqttConnectOptions options) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { MQTTBrokerInformation.URL });
        options.setUserName(MQTTBrokerInformation.USERNAME);
        options.setPassword(MQTTBrokerInformation.PASSWORD.toCharArray());

        // Reconexión automática
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);

        // Configuración de reintentos
        options.setConnectionTimeout(10); // segundos
        options.setKeepAliveInterval(60); // segundos
        options.setMaxReconnectDelay(128000); // milisegundos
        return options;
    }
}
