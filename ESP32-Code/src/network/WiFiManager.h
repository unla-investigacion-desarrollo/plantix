#ifndef WIFI_MANAGER_H
#define WIFI_MANAGER_H

#include "config/config.h"

class WiFiManager{
     public:
          void setupWifi();
          bool isConnected();
          void showIp();
};

#endif // WIFI_MANAGER_H