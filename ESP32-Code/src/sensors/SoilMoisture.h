#ifndef SOIL_MOISTURE_H
#define SOIL_MOISTURE_H

#include "config/config.h"

class SoilMoisture{
     private:
          //Fixed in ESP32 config
          int id; //id igual al de la BD
          int inputPin;
          int dryValue;
          int wetValue;
          //received from the broker
          float idealMoisture;
          //variables
          float actualMoisture;
          int actualRawValue;
          //active flag
          bool isActive;
     public:
          //Constructor:
          SoilMoisture(int sensorId, int pin, int dry, int wet);
          
          //metodos:
          void begin();
          void readSensor();

          //Getters
          int getId() const;
          int getInputPin() const;
          int getDryValue() const;
          int getWetValue() const;
          float getIdealMoisture() const;
          float getActualMoisture() const;
          int getActualRawValue() const;
          bool getIsActive() const;

          //Setters
          void setId(int sensorId);
          void setInputPin(int pin);
          void setDryValue(int dry);
          void setWetValue(int wet);
          void setIdealMoisture(float ideal);
          void setActualMoisture(float actual);
          void setActualRawValue(int raw);
          void setIsActive(bool active);
};

#endif // SOIL_MOISTURE_H