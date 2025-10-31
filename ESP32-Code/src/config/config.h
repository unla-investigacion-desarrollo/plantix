#ifndef CONFIG_H
#define CONFIG_H

// Libs (pueden quedarse aquí)
#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <time.h>
#include "DHT.h"
#include <cstdlib>

// ✅ DECLARACIONES con extern
extern const char *ssid;
extern const char *password;

extern const char *mqtt_broker;
extern const char *topic_dht11;
extern const char *topic_hw390;
extern const char *mqtt_username;
extern const char *mqtt_password;
extern const int mqtt_port;

extern WiFiClient espClient;
extern PubSubClient client;

// Constantes de calibración (pueden quedarse si son const)
const int DRY_1 = 3792;
const int WET_1 = 1279;
const int DRY_2 = 3785;
const int WET_2 = 1271;
const int DRY_3 = 3791;
const int WET_3 = 1280;

// ✅ Variables con extern
extern unsigned long lastPublish;
extern const long PUBLISH_INTERVAL;

#endif // CONFIG_H