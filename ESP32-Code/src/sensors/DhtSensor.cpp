#include "DhtSensor.h"

// Constructor
DhtSensor::DhtSensor(int sensorId, int pin, int dhtType)
     : id(sensorId), inputPin(pin), type(dhtType),
     dht(pin, dhtType), // inicializa el objeto DHT
     idealTemperature(0.0f), idealHumidity(0.0f),
     actualTemperature(0.0f), actualHumidity(0.0f), heatIndex(0.0f),
     isActive(false), lastReadSuccess(false) {} //lista de inicialización

// Métodos
void DhtSensor::begin(){ this->dht.begin(); }
bool DhtSensor::readSensor(){
     this->actualHumidity = this->dht.readHumidity();
     this->actualTemperature = this->dht.readTemperature();

     if (isnan(this->actualHumidity) || isnan(this->actualTemperature)) {
          this->lastReadSuccess = false;
          this->actualHumidity = 0.0f;
          this->actualTemperature = 0.0f;
          return false;
     } 
     this->heatIndex = this->dht.computeHeatIndex(this->actualTemperature, this->actualHumidity, false);
     this->lastReadSuccess = true;
     return true;
}

// Getters
int DhtSensor::getId() const{ return this->id; }
int DhtSensor::getInputPin() const{ return this->inputPin; }
int DhtSensor::getType() const{ return this->type; }
float DhtSensor::getIdealTemperature() const{ return this->idealTemperature; }
float DhtSensor::getIdealHumidity() const{ return this->idealHumidity; }
float DhtSensor::getActualTemperature() const{ return this->actualTemperature; }
float DhtSensor::getActualHumidity() const{ return this->actualHumidity; }
float DhtSensor::getHeatIndex() const{ return this->heatIndex; }
bool DhtSensor::getIsActive() const{ return this->isActive; }
bool DhtSensor::getLastReadSuccess() const{ return this->lastReadSuccess; }

// Setters
void DhtSensor::setId(int sensorId){ this->id = sensorId; }
void DhtSensor::setInputPin(int pin){ this->inputPin = pin; }
void DhtSensor::setType(int dhtType){ this->type = dhtType; }
void DhtSensor::setIdealTemperature(float idealTemp){ this->idealTemperature = idealTemp; }
void DhtSensor::setIdealHumidity(float idealHum){ this->idealHumidity = idealHum; }
void DhtSensor::setActualTemperature(float actualTemp){ this->actualTemperature = actualTemp; }
void DhtSensor::setActualHumidity(float actualHum){ this->actualHumidity = actualHum; }
void DhtSensor::setHeatIndex(float hic){ this->heatIndex = hic; }
void DhtSensor::setIsActive(bool active){ this->isActive = active; }
void DhtSensor::setLastReadSuccess(bool success){ this->lastReadSuccess = success; }

// Utility
const char* DhtSensor::get_type_string() const {
     switch(type) {
          case DHT11: return "DHT11";
          case DHT22: return "DHT22";
          case DHT21: return "DHT21";
          default: return "UNKNOWN";
     }
}