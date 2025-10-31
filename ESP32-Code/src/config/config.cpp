#include "config.h"

#include "config.h"

// ✅ DEFINICIONES (con valores)

// WiFi
const char *ssid = "Esp32"; 
const char *password = "seniasegura";

// MQTT Broker
const char *mqtt_broker = "10.30.217.212";
const char *topic_dht11 = "DHT11-topic";
const char *topic_hw390 = "HW390-topic";
const char *mqtt_username = "plantix";
const char *mqtt_password = "plantixiot";
const int mqtt_port = 1883;

// Clientes
WiFiClient espClient;
PubSubClient client(espClient);

// Intervals
unsigned long lastPublish = 0;
const long PUBLISH_INTERVAL = 10000;

// NOTA: Las constantes DRY_1, WET_1, etc. se quedan en el .h
// porque son const y no causan múltiples definiciones