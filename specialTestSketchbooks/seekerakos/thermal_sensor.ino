/*
An open-source LM35DZ Temperature Sensor for Arduino. This project will be enhanced on a regular basis
(cc) by Daniel Spillere Andrade , http://www.danielandrade.net
http://creativecommons.org/license/cc-gpl
*/

int pin = 0; // analog pin
int tempc = 0,tempf=0; // temperature variables
int samples[8]; // variables to make a better precision
int maxi = -100,mini = 100; // to start max/min temperature
int i;

void setup()
{
  Serial.begin(9600); // start serial communication
}

void loop()
{
  
  
for(i = 0;i<=7;i++) 
  {
  samples[i] = ( 5.0 * analogRead(pin) * 100.0) / 1024.0;
  tempc = tempc + samples[i];
  delay(1000);
  }



tempc = tempc/8.0; // better precision
tempf = (tempc * 9)/ 5 + 32; // converts to fahrenheit

if(tempc > maxi) {maxi = tempc;} // set max temperature
if(tempc < mini) {mini = tempc;} // set min temperature

Serial.print(tempc,DEC);
Serial.print(" Celsius, ");

Serial.print(tempf,DEC);
Serial.print(" fahrenheit -> ");

Serial.print(maxi,DEC);
Serial.print(" Max, ");
Serial.print(mini,DEC);
Serial.println(" Min");

tempc = 0;

delay(1000); // delay before loop
}
