#include "config/pins.h"
#include "config/config.h"
#include "network/WiFiManager.h"
#include "network/MQTTManager.h"
#include "utils/TimeUtils.h"
#include "utils/DisplayUtils.h"
#include "sensors/DhtSensor.h"
#include "sensors/SoilMoisture.h"
#include "actuators/ValveController.h"

// Crear instancias de los gestores
WiFiManager wifiManager;
MQTTManager mqttManager;
TimeUtils timeUtils;
DisplayUtils displayUtils;

// Crear instancia del sensor DHT11
DhtSensor DhtSensor1(4, DHTPIN, DHT11);

//Creamos instancias del HW390
SoilMoisture SoilMoisture1(1, HUMEDAD_SUSTRATO_1,DRY_1,WET_1);
// SoilMoisture SoilMoisture2(4, HUMEDAD_SUSTRATO_2,DRY_2,WET_2);
// SoilMoisture SoilMoisture3(5, HUMEDAD_SUSTRATO_3,DRY_3,WET_3);

// Crear instancia del controlador de válvula
ValveController ValveController1(5, RELAY_ELECTROVALVULA_1);
// ValveController ValveController2(2, RELAY_ELECTROVALVULA_2);
// ValveController ValveController3(3, RELAY_ELECTROVALVULA_3);

void configSensors(){
  DhtSensor1.setIdealHumidity(60.0f);
  DhtSensor1.setIdealTemperature(25.0f);
  DhtSensor1.setIsActive(true);
  SoilMoisture1.setIdealMoisture(50.0f);
  SoilMoisture1.setIsActive(true);
}

void setup() {
  Serial.begin(115200);

  // Configurar ADC
  analogSetAttenuation(ADC_11db);
  analogReadResolution(12);

  // Conectar a WiFi
  wifiManager.setupWifi();

  // Configuración NTP (Argentina UTC-3)
  timeUtils.setup();

  // Configurar MQTT
  mqttManager.setupMqtt();
  mqttManager.reconnectMqtt();

  // Iniciar sensor DHT11
  DhtSensor1.begin();

  // Iniciar sensores de humedad del suelo
  SoilMoisture1.begin();
  // SoilMoisture2.begin();
  // SoilMoisture3.begin();

  // Iniciar controladores de válvulas

  ValveController1.begin();
  // ValveController2.begin();
  // ValveController3.begin();

  displayUtils.showWelcomeMessage();

  configSensors();
}


void mostrarDatosSerial() {
  Serial.println("========================================");
  
  // Datos del suelo
  displayUtils.showFloorInfo(SoilMoisture1.getActualRawValue(), SoilMoisture1.getActualMoisture());
  // Barra de progreso visual
  displayUtils.showProgressBar(SoilMoisture1.getActualMoisture());
  
  // Estado de riego
  displayUtils.showIrrigationStatus(ValveController1.getIsOpen());

  // Datos ambientales
  displayUtils.showEnvironmentalData(DhtSensor1.getActualTemperature(), DhtSensor1.getActualHumidity(), DhtSensor1.getHeatIndex());

  Serial.println("----------------------------------------");
}

void leerSensoresYControlar() {
  // Leer humedad suelo y controlar riego
  SoilMoisture1.readSensor();

  // Control de la electroválvula
  bool necesitaRiego = (SoilMoisture1.getActualMoisture() < SoilMoisture1.getIdealMoisture());
  
  if (necesitaRiego && !ValveController1.getIsOpen()) {
    digitalWrite(ValveController1.getPin(), HIGH);
    ValveController1.open();
    Serial.println("💧 ACTIVANDO RIEGO - Suelo seco");
  } else if (!necesitaRiego && ValveController1.getIsOpen()) {
    digitalWrite(ValveController1.getPin(), LOW);
    ValveController1.close();
    Serial.println("🛑 DETENIENDO RIEGO - Suelo húmedo");
  }

  // Leer DHT11
  DhtSensor1.readSensor();

  // Mostrar datos por serial
  mostrarDatosSerial();
}

void publicarDatosDHT11() {
  // Obtener fecha y hora en formato DD-MM-YYYYTHH:MM:SS
  String timestamp = timeUtils.getTimestamp();
  mqttManager.publishDHT(DhtSensor1.getId(), DhtSensor1.getActualHumidity(), DhtSensor1.getActualTemperature(), timestamp.c_str());
  mqttManager.publishHW390(SoilMoisture1.getId(), SoilMoisture1.getActualMoisture(), timestamp.c_str());
}
void loop() {
  if (!client.connected()) {
    mqttManager.reconnectMqtt();
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