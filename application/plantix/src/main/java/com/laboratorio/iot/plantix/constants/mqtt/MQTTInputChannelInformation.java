package com.laboratorio.iot.plantix.constants.mqtt;

/**
 * Contains constants for all MQTT input channels.
 * These channels are used to route incoming MQTT messages to the appropriate handlers.
 */
public class MQTTInputChannelInformation {
    // Common channel where all incoming MQTT messages arrive first
    public static final String COMMON_CHANNEL = "commonInputChannel";
    
    // Sensor data channels
    public static final String SENSOR_DATA_CHANNEL = "sensorDataInputChannel";
    
    // Electrovalve control channels
    public static final String ELECTROVALVE_OPEN_CHANNEL = "electrovalveOpenInputChannel";
    public static final String ELECTROVALVE_CLOSE_CHANNEL = "electrovalveCloseInputChannel";
    
    // Error handling channel
    public static final String ERROR_CHANNEL = "errorInputChannel";
    
    // Private constructor to prevent instantiation
    private MQTTInputChannelInformation() {
        throw new IllegalStateException("Utility class");
    }
}
