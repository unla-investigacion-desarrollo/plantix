#ifndef DISPLAY_UTILS_H
#define DISPLAY_UTILS_H

#include "config/config.h"

class DisplayUtils {
public:
     static void showWelcomeMessage();
     static void showErrorMessage(const String &message);
     static void showFloorInfo(int rawSoil, float soilMoisture);
     static void showProgressBar(int percentage);
     static void showIrrigationStatus(bool isIrrigating);
     static void showEnvironmentalData(float temperature, float humidity, float hic);
};

#endif // DISPLAY_UTILS_H