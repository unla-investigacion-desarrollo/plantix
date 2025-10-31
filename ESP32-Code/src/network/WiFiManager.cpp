#include "WiFiManager.h"

void WiFiManager::setupWifi(){
     WiFi.begin(ssid, password);
     Serial.print("Connecting to WiFi");
     while (isConnected() == false) {
          delay(500);
          Serial.print(".");
     }
     Serial.println("");
     Serial.println("WiFi Connected");
}

bool WiFiManager::isConnected(){
    return WiFi.status() == WL_CONNECTED;
}

void WiFiManager::showIp(){
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());
}    
