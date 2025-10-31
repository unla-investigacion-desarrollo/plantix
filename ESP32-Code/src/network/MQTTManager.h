#ifndef MQTT_MANAGER_H
#define MQTT_MANAGER_H

#include "config/config.h"


class MQTTManager{
     public: 
          // setup MQTT
          void setupMqtt();
          void reconnectMqtt();

          // callBack for MQTT messages
          void mqttSubscribe(char* topic);
          void mqttCallback(char* topic, byte* payload, unsigned int length);
          
          // publish MQTT
          void publishMqtt(const char* topic, const char* payload);
          void publishDHT(int sensorId, float humidity, float temperature, const char* timestamp);
          void publishHW390(int sensorId, float moisture, const char* timestamp);
};

#endif // MQTT_MANAGER_H