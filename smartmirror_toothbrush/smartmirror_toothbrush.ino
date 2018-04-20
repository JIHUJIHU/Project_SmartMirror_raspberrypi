#include <SoftwareSerial.h>

#define RX_PIN 0
#define TX_PIN 1
#define VIB_PIN 2

SoftwareSerial BT(TX_PIN, RX_PIN);

boolean flag = false;
unsigned long pretime = 0;
int cnt = 0;

void setup() {
  Serial.begin(9600);
  pinMode(VIB_PIN, INPUT);
  BT.begin(9600);
}

void loop() {
  if(millis()-pretime >= 500) {
    pretime1 = millis();
    int a = digitalRead(VIB_PIN);     //- 진동값 변수저장
    Serial.println(a);
    if(a) {
      BT.print("@VIB,START,#");
      Serial.println("Vibration Sensing");
      cnt = 0;
      if(!flag) flag = true;
    } else {
      cnt++;
      Serial.println("Nothing");
    }
    if(cnt > 3 && flag) {
      BT.print("@VIB,END,#");
      Serial.println("OFF");
      flag = false;
    }
  }
  
  if(BT.available() > 0)
  {
    Serial.print((char)BT.read());
  }

  /*if(millis()-pretime >= 1000) {
    pretime = millis();
    cds_value = analogRead(CDS_PIN);
    Serial.print("cds= ");
    Serial.println(cds_value);
  
    if(cds_value > 50) {
      digitalWrite(LED_PIN, HIGH);
      Serial.println("LED ON");
    } else {
      digitalWrite(LED_PIN, LOW);
      Serial.println("LED OFF");
    }
  }*/
}

float getDistance() {
  float duration, distance;

  digitalWrite(BT_TRIG, HIGH);
  delay(10);
  digitalWrite(BT_TRIG, LOW);

  duration = pulseIn(BT_ECHO, HIGH);
  distance = ((float)(340*duration)/10000)/2;
  //Serial.print(distance);
  //Serial.println("cm");
  
  return distance;
}

