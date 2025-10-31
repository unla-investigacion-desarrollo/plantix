#include "DisplayUtils.h"

void DisplayUtils::showWelcomeMessage(){
     Serial.println("🌱 Plantix Iniciado 🌱");
}

void DisplayUtils::showErrorMessage(const String &message) {
     Serial.print("❌ Error: ");
     Serial.println(message);
}

void DisplayUtils::showFloorInfo(int rawSoil, float soilMoisture) {
     Serial.print("🌱 Información del Suelo:");
     Serial.print(" - Raw: ");
     Serial.print(rawSoil);
     Serial.print(", Humedad: ");
     Serial.println(soilMoisture);
}

void DisplayUtils::showProgressBar(int percentage) {
     Serial.print(" [");
     int barras = map(percentage, 0, 100, 0, 20);
     for (int i = 0; i < 20; i++) {
          if (i < barras) Serial.print("█");
          else Serial.print("░");
     }
     Serial.print("]");
}

void DisplayUtils::showIrrigationStatus(bool isIrrigating) {
     Serial.print("Estado de Riego: ");
     Serial.println(isIrrigating ? "💧 REGANDO" : "✅ DETENIDO");
}

void DisplayUtils::showEnvironmentalData(float temperature, float humidity, float hic) {
     if (!isnan(humidity) && !isnan(temperature)) {
          Serial.print("🌡️  AMBIENTE - Hum: ");
          Serial.print(humidity);
          Serial.print("% | Temp: ");
          Serial.print(temperature);
          Serial.print("°C | Sensación: ");
          Serial.print(hic);
          Serial.println("°C");
     } else {
          Serial.println("❌ Error leyendo datos ambientales");
     }
}