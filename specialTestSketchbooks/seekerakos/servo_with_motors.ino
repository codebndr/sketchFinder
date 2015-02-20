#include <Servo.h>

Servo myservo;
int potpin=0;
int val;
int eksodos;
int switchPin = 2;    // switch input
int motor1Pin = 3;    // H-bridge leg 1 (pin 2, 1A)
int motor2Pin = 4;    // H-bridge leg 2 (pin 7, 2A)
int enablePin = 9;    // H-bridge enable pin
int ledPin = 13; 

void setup()
{
  Serial.begin(9600);
  myservo.attach(8);
  pinMode(motor1Pin, OUTPUT); 
    pinMode(motor2Pin, OUTPUT); 
    pinMode(enablePin, OUTPUT);
    pinMode(ledPin, OUTPUT);
     // set enablePin high so that motor can turn on:
    digitalWrite(enablePin, HIGH); 

    
}

void loop()
{
  val = eksodos;
  val= analogRead(potpin);
  val = map(val,0,1023,0,180);
  
  if (val != eksodos){
    Serial.println(val); 
    eksodos = val ;
  }  
  myservo.write(val);
  delay(15);
  if (digitalRead(switchPin) == HIGH) {
      digitalWrite(motor1Pin, LOW);   // set leg 1 of the H-bridge low
      digitalWrite(motor2Pin, HIGH);  // set leg 2 of the H-bridge high
    } 
    // if the switch is low, motor will turn in the other direction:
    else {
      digitalWrite(motor1Pin, HIGH);  // set leg 1 of the H-bridge high
      digitalWrite(motor2Pin, LOW);   // set leg 2 of the H-bridge low
    }
}



