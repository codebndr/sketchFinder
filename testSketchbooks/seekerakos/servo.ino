#include <Servo.h>

Servo myservo;

void setup()
{
  myservo.attach(9);
}

void loop()
{
  myservo.write(170);
  delay(1000);
  myservo.write(30);
  delay(1000);
}
