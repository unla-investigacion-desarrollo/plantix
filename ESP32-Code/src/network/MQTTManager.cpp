#include "MQTTManager.h"

void MQTTManager::setupMqtt(){
     client.setServer(mqtt_broker, mqtt_port);

     reconnectMqtt();
}

void MQTTManager::reconnectMqtt(){
     while (!client.connected()) {
          Serial.print("Conectando a MQTT...");
          String client_id = "esp32-client-" + String(WiFi.macAddress());
          
          if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
               Serial.println("conectado");
          } else {
               Serial.print("falló, rc=");
               Serial.print(client.state());
               Serial.println(" intentando en 5 segundos");
               delay(5000);
          }
     }
}

void MQTTManager::mqttSubscribe(char* topic){
     client.subscribe(topic);
     Serial.print("Suscrito al topic: ");
     Serial.println(topic);
}

void MQTTManager::mqttCallback(char* topic, byte* payload, unsigned int length){
     Serial.print("Mensaje recibido en el topic: ");
     Serial.print(topic);
     Serial.print(". Mensaje: ");
     for (unsigned int i = 0; i < length; i++) {
          Serial.print((char)payload[i]);
     }
     Serial.println();
}

void MQTTManager::publishMqtt(const char* topic, const char* payload){
     if (client.publish(topic, payload)) {
          Serial.println("Mensaje publicado en el topic:");
          Serial.println(topic);
          Serial.println("Payload:");
          Serial.println(payload);
     } else {
          Serial.println("Error publicando en MQTT");
     }
}

void MQTTManager::publishDHT(int sensorId, float humidity, float temperature, const char* timestamp){
     StaticJsonDocument<200> doc;
     doc["sensorId"] = sensorId;
     doc["timestamp"] = timestamp;
     
     JsonObject data = doc.createNestedObject("data");
     // Convertir a string con 2 decimales
     char tempStr[8];
     char humStr[8];
     dtostrf(temperature, 1, 2, tempStr);
     dtostrf(humidity, 1, 2, humStr);
     
     data["temperature"] = tempStr;
     data["humidity"] = humStr;

     // Serializar y publicar
     char jsonBuffer[200];
     serializeJson(doc, jsonBuffer);
     
     publishMqtt(topic_dht11, jsonBuffer);
}

void MQTTManager::publishHW390(int sensorId, float moisture, const char* timestamp){
     StaticJsonDocument<200> doc;
     doc["sensorId"] = sensorId;
     doc["timestamp"] = timestamp;
     
     JsonObject data = doc.createNestedObject("data");
     // Convertir a string con 2 decimales
     char moistureStr[8];
     dtostrf(moisture, 1, 2, moistureStr);
     
     data["moisture"] = moistureStr;

     // Serializar y publicar
     char jsonBuffer[200];
     serializeJson(doc, jsonBuffer);
     
     publishMqtt(topic_hw390, jsonBuffer);
}

