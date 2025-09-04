package com.laboratorio.iot.plantix.configuration.mqtt;

import com.laboratorio.iot.plantix.constants.mqtt.MQTTBrokerInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTInputChannelInformation;
import com.laboratorio.iot.plantix.constants.mqtt.MQTTOutputChannelInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;

/**
 * Configuration for MQTT inbound (subscribing) messages.
 */
@Configuration
public class MQTTInboundConfiguration {
    private static final int COMPLETION_TIMEOUT = 5000;
    private static final int DELIVER_ONLY_ONCE_AND_CONFIRM = 2;
    private final MqttPahoClientFactory mqttPahoClientFactory;

    public MQTTInboundConfiguration(MqttPahoClientFactory mqttPahoClientFactory) {
        this.mqttPahoClientFactory = mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel commonInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(false);

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        MQTTBrokerInformation.URL,
                        "receiverClient",
                        mqttPahoClientFactory,
                        MQTTBrokerInformation.DHT11_TOPIC,
                        MQTTBrokerInformation.SENSOR_SUBSTRATE_MOISTURE_TOPIC,
                        MQTTBrokerInformation.ELECTROVALVE_OPEN_TOPIC,
                        MQTTBrokerInformation.ELECTROVALVE_CLOSE_TOPIC,
                        MQTTBrokerInformation.ERRORS_TOPIC);
        
        adapter.setCompletionTimeout(COMPLETION_TIMEOUT);
        adapter.setConverter(converter);
        adapter.setQos(DELIVER_ONLY_ONCE_AND_CONFIRM);
        adapter.setOutputChannel(commonInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = MQTTInputChannelInformation.COMMON_CHANNEL)
    public HeaderValueRouter mqttTopicRouter() {
        HeaderValueRouter router = new HeaderValueRouter("mqtt_receivedTopic");
        
        // Route DHT11 data
        router.setChannelMapping(
                MQTTBrokerInformation.DHT11_TOPIC,
                MQTTInputChannelInformation.DHT11_CHANNEL);
                
        // Route substrate moisture sensor data
        router.setChannelMapping(
                MQTTBrokerInformation.SENSOR_SUBSTRATE_MOISTURE_TOPIC,
                MQTTInputChannelInformation.SENSOR_SUBSTRATE_MOISTURE_CHANNEL);
                
        // Route electrovalve open commands
        router.setChannelMapping(
                MQTTBrokerInformation.ELECTROVALVE_OPEN_TOPIC,
                MQTTInputChannelInformation.ELECTROVALVE_OPEN_CHANNEL);
                
        // Route electrovalve close confirmations
        router.setChannelMapping(
                MQTTBrokerInformation.ELECTROVALVE_CLOSE_TOPIC,
                MQTTInputChannelInformation.ELECTROVALVE_CLOSE_CHANNEL);
        
     // Route error confirmation
        router.setChannelMapping(
                MQTTBrokerInformation.ERRORS_TOPIC,
                MQTTInputChannelInformation.ERROR_CHANNEL);
                
        return router;
    }
}
