#include "SoilMoisture.h"

// Constructor
SoilMoisture::SoilMoisture(int sensorId, int pin, int dry, int wet)
     : id(sensorId), inputPin(pin), dryValue(dry), wetValue(wet),
     idealMoisture(0.0f), actualMoisture(0.0f), 
     actualRawValue(0), isActive(false) {} //lista de inicialización


// Función map para floats
float mapFloat(float x, float in_min, float in_max, float out_min, float out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}


// Metodos:
void SoilMoisture::begin(){ pinMode(this->inputPin, INPUT); }

void SoilMoisture::readSensor(){
     this->actualRawValue = analogRead(this->inputPin);
     this->actualMoisture = mapFloat(this->actualRawValue, this->dryValue, this->wetValue, 0.0f, 100.0f);// Map to percentage
     this->actualMoisture = constrain(this->actualMoisture, 0.0f, 100.0f);// si el valor se sale de rango, lo ajusta
}


// Getters
int SoilMoisture::getId() const{ return this->id; }
int SoilMoisture::getInputPin() const{ return this->inputPin; }
int SoilMoisture::getDryValue() const{ return this->dryValue; }
int SoilMoisture::getWetValue() const{ return this->wetValue; }
float SoilMoisture::getIdealMoisture() const{ return this->idealMoisture; }
float SoilMoisture::getActualMoisture() const{ return this->actualMoisture; }
int SoilMoisture::getActualRawValue() const{ return this->actualRawValue; }
bool SoilMoisture::getIsActive() const{ return this->isActive; }


// Setters
void SoilMoisture::setId(int sensorId){ this->id = sensorId; }
void SoilMoisture::setInputPin(int pin){ this->inputPin = pin; }
void SoilMoisture::setDryValue(int dry){ this->dryValue = dry; }
void SoilMoisture::setWetValue(int wet){ this->wetValue = wet; }
void SoilMoisture::setIdealMoisture(float ideal){ this->idealMoisture = ideal; }
void SoilMoisture::setActualMoisture(float actual){ this->actualMoisture = actual; }
void SoilMoisture::setActualRawValue(int raw){ this->actualRawValue = raw; }
void SoilMoisture::setIsActive(bool active){ this->isActive = active; }