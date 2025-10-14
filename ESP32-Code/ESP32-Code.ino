#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <time.h>
#include "DHT.h"

// Pines del ESP32
#define HUMEDAD_SUSTRATO 34
#define RELAY_ELECTROVALVULA 4
#define DHTPIN 16
#define DHTTYPE DHT11

// WiFi
const char *ssid = "MovistarFibra-101970";
const char *password = "2F58v7sDwhcFEDMr5sw2";

// MQTT Broker
const char *mqtt_broker = "192.168.1.39";
const char *topic_dht11 = "DHT11-topic";
const char *mqtt_username = "plantix";
const char *mqtt_password = "plantixiot";
const int mqtt_port = 1883;

WiFiClient espClient;
PubSubClient client(espClient);

// Valores de calibración del suelo
const int SECO = 3792;
const int HUMEDO = 1279;
const int UMBRAL_RIEGO = 50;

// Variables
int humedadSustrato = 0;
bool electrovalvulaActiva = false;
float temperatura = 0;
float humedad = 0;
float hic = 0;

DHT dht(DHTPIN, DHTTYPE);

// Tiempos
unsigned long lastPublish = 0;
const long PUBLISH_INTERVAL = 10000; // Publicar cada 10 segundos

void setup() {
  Serial.begin(115200);

  // Configurar pines
  pinMode(HUMEDAD_SUSTRATO, INPUT);
  pinMode(RELAY_ELECTROVALVULA, OUTPUT);
  digitalWrite(RELAY_ELECTROVALVULA, LOW);

  // Configurar ADC
  analogSetAttenuation(ADC_11db);
  analogReadResolution(12);

  // Conectar a WiFi
  setup_wifi();

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

  // Configurar MQTT
  client.setServer(mqtt_broker, mqtt_port);
  reconnectMQTT();

  // Iniciar DHT
  dht.begin();

  Serial.println("Sistema de riego automático iniciado");
  Serial.println("=====================================");
}

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Conectando a ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("");
  Serial.println("WiFi conectado");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnectMQTT() {
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

void loop() {
  if (!client.connected()) {
    reconnectMQTT();
  }
  client.loop();

  // Leer sensores y controlar riego
  leerSensoresYControlar();

  // Publicar por MQTT cada 10 segundos
  if (millis() - lastPublish > PUBLISH_INTERVAL) {
    lastPublish = millis();
    publicarDatosDHT11();
  }
  
  delay(2000);
}

void leerSensoresYControlar() {
  // Leer humedad suelo y controlar riego
  int valueHW390 = analogRead(HUMEDAD_SUSTRATO);
  humedadSustrato = map(valueHW390, SECO, HUMEDO, 0, 100);
  humedadSustrato = constrain(humedadSustrato, 0, 100);

  // Control de la electroválvula
  bool necesitaRiego = (humedadSustrato < UMBRAL_RIEGO);
  
  if (necesitaRiego && !electrovalvulaActiva) {
    digitalWrite(RELAY_ELECTROVALVULA, HIGH);
    electrovalvulaActiva = true;
    Serial.println("💧 ACTIVANDO RIEGO - Suelo seco");
  } else if (!necesitaRiego && electrovalvulaActiva) {
    digitalWrite(RELAY_ELECTROVALVULA, LOW);
    electrovalvulaActiva = false;
    Serial.println("🛑 DETENIENDO RIEGO - Suelo húmedo");
  }

  // Leer DHT11
  humedad = dht.readHumidity();
  temperatura = dht.readTemperature();
  
  if (isnan(humedad) || isnan(temperatura)) {
    Serial.println("❌ Error leyendo sensor DHT11!");
  } else {
    hic = dht.computeHeatIndex(temperatura, humedad, false);
  }

  // Mostrar datos por serial
  mostrarDatosSerial(valueHW390, humedadSustrato);
}

void publicarDatosDHT11() {
  // Obtener fecha y hora en formato DD-MM-YYYYTHH:MM:SS
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    Serial.println("❌ Error obteniendo hora local");
    return;
  }

  char timestamp[20];
  strftime(timestamp, sizeof(timestamp), "%d-%m-%YT%H:%M:%S", &timeinfo);

  // Crear JSON con el formato especificado
  StaticJsonDocument<200> doc;
  doc["sensorId"] = "4";
  doc["timestamp"] = timestamp;
  
  JsonObject data = doc.createNestedObject("data");
  // Convertir a string con 2 decimales
  char tempStr[8];
  char humStr[8];
  dtostrf(temperatura, 1, 2, tempStr);
  dtostrf(humedad, 1, 2, humStr);
  
  data["temperature"] = tempStr;
  data["humidity"] = humStr;

  // Serializar y publicar
  char jsonBuffer[200];
  serializeJson(doc, jsonBuffer);
  
  if (client.publish(topic_dht11, jsonBuffer)) {
    Serial.println("✅ JSON publicado en DHT11-topic:");
    Serial.println(jsonBuffer);
  } else {
    Serial.println("❌ Error publicando en MQTT");
  }
}

void mostrarDatosSerial(int valorRaw, int humedadPercent) {
  Serial.println("========================================");
  
  // Datos del suelo
  Serial.print("🌱 SUELO - Raw: ");
  Serial.print(valorRaw);
  Serial.print(" | Humedad: ");
  Serial.print(humedadPercent);
  Serial.print("%");
  
  // Barra de progreso visual
  Serial.print(" [");
  int barras = map(humedadPercent, 0, 100, 0, 20);
  for (int i = 0; i < 20; i++) {
    if (i < barras) Serial.print("█");
    else Serial.print("░");
  }
  Serial.print("]");
  
  // Estado de riego
  if (electrovalvulaActiva) {
    Serial.println(" | 💧 REGANDO");
  } else {
    Serial.println(" | ✅ DETENIDO");
  }

  // Datos ambientales
  if (!isnan(humedad) && !isnan(temperatura)) {
    Serial.print("🌡️  AMBIENTE - Hum: ");
    Serial.print(humedad);
    Serial.print("% | Temp: ");
    Serial.print(temperatura);
    Serial.print("°C | Sensación: ");
    Serial.print(hic);
    Serial.println("°C");
  }
  
  Serial.println("----------------------------------------");
}