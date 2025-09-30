package com.laboratorio.iot.plantix.constants.mqtt;

/**
 * Contains all MQTT broker configuration and topic information.
 * Topics follow the structure: plantix/<context>/<field_id>/<device_type>/<action>
 */
public class MQTTBrokerInformation {
    // Broker connection details
    public static final String URL = System.getenv("MQTT_URL") != null ? 
            System.getenv("MQTT_URL") : "tcp://docker-broker-mosquitto-1:1883";
    public static final String USERNAME = System.getenv("MQTT_USERNAME");
    public static final String PASSWORD = System.getenv("MQTT_PASSWORD");

    // Base topic paths
    private static final String FIELD_TOPIC =  "field"; // + is wildcard for field ID

    // Incoming topics (subscriptions)
    public static final String DHT11_TOPIC = "DHT11-topic";
    public static final String ELECTROVALVE_CLOSE_TOPIC = "electrovalve-close-topic";
    public static final String ERRORS_TOPIC =  "errors-topic";
    public static final String HW390_TOPIC = "HW390-topic";

    // Outgoing topics (publications)
    public static final String REQUEST_SENSOR_DATA_TOPIC = "request-sensor-data-topic";
    public static final String ELECTROVALVE_OPEN_TOPIC = "electrovalve-open-topic";
    public static final String SENSOR_SUBSTRATE_MOISTURE_TOPIC = "sensor-substrate-moisture-topic";

    // QoS levels
    public static final int QOS_AT_MOST_ONCE = 0;
    public static final int QOS_AT_LEAST_ONCE = 1;
    public static final int QOS_EXACTLY_ONCE = 2;

    // Default QoS for different message types
    public static final int DEFAULT_SENSOR_QOS = QOS_AT_LEAST_ONCE;
    public static final int DEFAULT_CONTROL_QOS = QOS_EXACTLY_ONCE;
    public static final int DEFAULT_ERROR_QOS = QOS_AT_LEAST_ONCE;
}
