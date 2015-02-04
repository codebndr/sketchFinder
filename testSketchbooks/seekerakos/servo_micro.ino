#include <Servo.h> 

Servo myservo;

void setup() 
{ 
  myservo.attach(8);
  myservo.writeMicroseconds(1500);  // set servo to mid-point
} 

void loop() {
myservo.writeMicroseconds(1700);
delay(1000);
myservo.writeMicroseconds(900);
delay(1000);
} 
