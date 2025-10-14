package com.laboratorio.iot.plantix.validator;

import java.time.LocalDateTime;

import com.laboratorio.iot.plantix.dtos.mqtt.dht11.DHT11Data;
import com.laboratorio.iot.plantix.entities.Sensor;

public class SensorHistoryValidator {
    public static boolean thisSensorIsNotValid(Sensor sensor) {
        return sensor == null;
    }

    public static boolean thisTimestampIsNotValid(LocalDateTime localDateTime) {
        return localDateTime == null;
    }

    public static boolean thisDataIsNotValid(String data) {
        return data == null || data.isBlank();
    }

    public static boolean thisDHT11DataIsNotValid(DHT11Data dht11Data) {
        return dht11Data == null;
    }

    public static boolean thisHW390DataIsNotValid(String hw390Data) {
        return hw390Data == null || hw390Data.isBlank();
    }
}