#ifndef DHT_SENSOR_H
#define DHT_SENSOR_H

#include "config/config.h"

class DhtSensor{
     private:
          //Fixed in ESP32 config
          int id;
          int inputPin;
          int type; //DHT11, DHT22, etc.
          DHT dht;
          // received from the broker
          float idealTemperature;
          float idealHumidity;
          // variables
          float actualTemperature;
          float actualHumidity;
          float heatIndex;
          //flags
          bool isActive;
          bool lastReadSuccess;
     public:
          // Constructor:
          DhtSensor(int sensorId, int pin, int dhtType);

          // metodos:
          void begin();
          bool readSensor();

          // Getters
          int getId() const;
          int getInputPin() const;
          int getType() const;
          float getIdealTemperature() const;
          float getIdealHumidity() const;
          float getActualTemperature() const;
          float getActualHumidity() const;
          float getHeatIndex() const;
          bool getIsActive() const;
          bool getLastReadSuccess() const;

          // Setters
          void setId(int sensorId);
          void setInputPin(int pin);
          void setType(int dhtType);
          void setIdealTemperature(float idealTemp);
          void setIdealHumidity(float idealHum);
          void setActualTemperature(float actualTemp);
          void setActualHumidity(float actualHum);
          void setHeatIndex(float hic);
          void setIsActive(bool active);
          void setLastReadSuccess(bool success);

          // Utility
          const char* get_type_string() const;
};

#endif // DHT_SENSOR_H