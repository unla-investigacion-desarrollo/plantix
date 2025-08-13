package com.laboratorio.iot.plantix.constants.mqtt;

public class MQTTBrokerInformation {
    public static String URL = "tcp://docker-broker-mosquitto-1:1883";
    public static String USERNAME = System.getenv("MQTT_USERNAME");
    public static String PASSWORD = System.getenv("MQTT_PASSWORD");
    public static String DHT11_TEMP_TOPIC = "DHT11-temp-topic";
    public static String DHT11_HUMIDITY_TOPIC = "DHT11-humidity-topic";
    public static String SENSOR_SUBSTRATE_MOISTURE_TOPIC = "sensor-substrate-moisture-topic";
    public static String ELECTROVALVE_OPEN_TOPIC = "electrovalve-open-topic";
    public static String ELECTROVALVE_CLOSE_TOPIC = "electrovalve-close-topic";
}
