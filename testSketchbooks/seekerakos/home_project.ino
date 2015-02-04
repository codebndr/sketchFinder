#include <Servo.h>
#include <VirtualWire.h>  

int photo=2;
int wifi=2;
Servo myserv;
int buzzer=8;
int relay=7;
#define sonictrig 12
#define sonicecho 11
int red=10;
int green=6;
int Light = 900;

void setup() {
 
  Serial.begin (9600);
  pinMode(sonictrig, OUTPUT);
  pinMode(sonicecho, INPUT);
  pinMode(buzzer, OUTPUT);
 pinMode(relay, OUTPUT); 
 vw_set_ptt_inverted(true); 
    vw_setup(2000);	 
    vw_set_rx_pin(2);
    vw_rx_start();   
  }

void loop() {
  int duration, distance;
  digitalWrite(sonictrig, HIGH);
  delayMicroseconds(1000);
  digitalWrite(sonictrig, LOW);
  duration = pulseIn(sonicecho, HIGH);
  distance = (duration/2) / 29.1;
  if (distance >= 100 || distance <= 0){
    void loop()
{
  
  
  
    uint8_t buf[VW_MAX_MESSAGE_LEN];
    uint8_t buflen = VW_MAX_MESSAGE_LEN;
    if (vw_get_message(buf, &buflen)) // Non-blocking
    {
	int i;
	for (i = 0; i < buflen; i++)
	{
	    Serial.print(buf[i]);
        if(buf[i] == '1'){myserv.writeMicroseconds(1500);
             pinMode(green,HIGH}
	}
   }
   pinMode(green,LOW);
   pinMode(red,HIGH);
   delay(2000);
   digitalWrite(red, LOW);
   myserv.writeMicroseconds(300);
  else {
     buzz(buzzer, 2500, 500);
     if(analogRead(LightPin) >Light)
  {
    digitalWrite(relay, HIGH);
    delay(10000)
    if(analogRead(LightPin) >Light)
  {
    digitalWrite(relay, HIGH);
    
  }
   digitalWrite(relay, LOW);
  }
  else
  { 
    digitalWrite(relay, LOW);
  }
     
  }
  
}

void buzz(int targetPin, long frequency, long length) {
  long delayValue = 1000000/frequency/2; 
  long numCycles = frequency * length/ 1000; 
 for (long i=0; i < numCycles; i++){ 
    digitalWrite(targetPin,HIGH); 
    delayMicroseconds(delayValue); 
    digitalWrite(targetPin,LOW); 
    delayMicroseconds(delayValue); 
  }
}

