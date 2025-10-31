#include "ValveController.h"
//Constructor
ValveController::ValveController(int _id,int _pin): id(_id), pin(_pin), isOpen(false) {}

//Metodos
void ValveController::begin() {
     pinMode(pin, OUTPUT);
     close(); // Ensure valve is closed at start
}
void ValveController::open() {
     digitalWrite(pin, HIGH); // Assuming HIGH opens the valve
     this->isOpen = true;
}
void ValveController::close() {
     digitalWrite(pin, LOW); // Assuming LOW closes the valve
     this->isOpen = false;
}

//Getters
int ValveController::getId() const { return this->id; }
int ValveController::getPin() const { return this->pin; }
bool ValveController::getIsOpen() const { return this->isOpen; }

//Setter
void ValveController::setPin(int _pin) { this->pin = _pin; }
