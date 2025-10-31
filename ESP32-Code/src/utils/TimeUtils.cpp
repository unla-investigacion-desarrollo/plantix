#include "TimeUtils.h"

void TimeUtils::setup() {
    // Configuración NTP (Argentina UTC-3)
     configTime(-3 * 3600, 0, "pool.ntp.org", "time.nist.gov");

    // Esperar sincronización de hora
    Serial.println("Esperando sincronización NTP...");
    struct tm timeinfo;
    while (!getLocalTime(&timeinfo)) {
        delay(1000);
        Serial.print(".");
    }
    Serial.println("\nHora sincronizada con NTP");
}

String TimeUtils::getTimestamp() {
    struct tm timeinfo;
    if (!getLocalTime(&timeinfo)) {
        Serial.println("❌ Error obteniendo hora local");
        return "";
    }

    char timestamp[20];
    strftime(timestamp, sizeof(timestamp), "%d-%m-%YT%H:%M:%S", &timeinfo);
    return String(timestamp);
}