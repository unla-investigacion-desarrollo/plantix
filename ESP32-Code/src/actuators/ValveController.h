#ifndef VALVE_CONTROLLER_H
#define VALVE_CONTROLLER_H

#include "config/config.h"

class ValveController {
     private:
          int id;
          int pin;
          bool isOpen;
          
     public:
          //Contructor:
          ValveController(int _id,int _pin);

          //metodos:
          void begin();
          void open();
          void close();

          //getter
          int getId() const;
          int getPin() const;
          bool getIsOpen() const;

          //Setter
          void setPin(int _pin);
};
#endif // VALVE_CONTROLLER_H