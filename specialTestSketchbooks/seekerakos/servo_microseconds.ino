#include <Servo.h>

Servo myserv;

void setup()
{
  myserv.attach(8);
}

void loop()
{
   myserv.writeMicroseconds(1500);
   delay(1000);
    myserv.writeMicroseconds(300);
    delay(1000);
     
}
    
