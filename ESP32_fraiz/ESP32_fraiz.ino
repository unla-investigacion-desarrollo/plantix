 #include "DHT.h"
 
 //pines del ESP32
 #define sensorHumSus1 34
 #define sensorTemp1 32
 #define rele1 25

DHT dht(sensorTemp1, DHT11);

void setup() {
  Serial.begin(115200);
  dht.begin();

  pinMode(sensorHumSus1, INPUT);
  pinMode(sensorTemp1, INPUT);
  pinMode(rele1, OUTPUT);
  
}

void loop() {
  
  int valorSensorHumSus1 = analogRead(sensorHumSus1);
  int humedadSustrato1 = (int) map (valorSensorHumSus1, 3500, 1300, 0, 100);//mapeo de analogico a porcentaje 0 a 100. Valor minimo real del HW390 es 3500 y maximo real 1300
  humedadSustrato1 = constrain(humedadSustrato1, 0, 100);//limita el minimo en 0 y el maximo en 100

  float humedad = dht.readHumidity();
  float temperatura = dht.readTemperature();

  if(humedadSustrato1 < 50){
    digitalWrite(rele1, HIGH);
    Serial.println("Electrovalvula activada");
  }else{
    digitalWrite(rele1, LOW);
    Serial.println("Electrovalvula desactivada");
  }

  Serial.print("Humedad: ");
  Serial.print(humedad);
  Serial.println("% ");

  Serial.print("Temperatura: ");
  Serial.print(temperatura);
  Serial.println("°C "); 

  Serial.print("Humedad sustrato:");
  Serial.print(humedadSustrato1);
  Serial.println("%");

  Serial.print("\n\n\n\n\n");
 delay(1000); //demora entre lecturas
}
