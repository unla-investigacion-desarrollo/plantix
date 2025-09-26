package com.laboratorio.iot.plantix.validator;

import com.laboratorio.iot.plantix.entities.Field;

public class SensorValidator {
    public static boolean thisNameIsNotValid(String name) {
        return name.isBlank();
    }
    public static boolean thisFieldIsNotValid(Field field) {
        return field == null;
    }
    public static boolean thisMeasurementIntervalIsNotValid(Integer measurementInterval) {
        return measurementInterval < 0;
    }
}
