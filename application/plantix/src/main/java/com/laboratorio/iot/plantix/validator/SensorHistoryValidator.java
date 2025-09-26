package com.laboratorio.iot.plantix.validator;

import com.laboratorio.iot.plantix.entities.Sensor;
import java.time.LocalDateTime;

public class SensorHistoryValidator {
    public static boolean thisSensorIsNotValid(Sensor sensor) {
        return sensor == null;
    }
    public static boolean thisTimestampIsNotValid(LocalDateTime localDateTime) {
        return localDateTime == null;
    }
    public static boolean thisDataIsNotValid(String data) {
        return data.isBlank() || !data.contains(",");
    }
}
