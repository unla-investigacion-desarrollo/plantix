package com.laboratorio.iot.plantix.validator;

import com.laboratorio.iot.plantix.dtos.mqtt.dht11.DHT11Data;
import com.laboratorio.iot.plantix.entities.Sensor;
import java.time.LocalDateTime;

public class SensorHistoryValidator {
    public static boolean thisSensorIsNotValid(Sensor sensor) {
        return sensor == null;
    }
    public static boolean thisTimestampIsNotValid(LocalDateTime localDateTime) {
        return localDateTime == null;
    }
    public static boolean thisDHT11DataIsNotValid(DHT11Data dht11Data) {
        return dht11Data == null;
    }
}
