int servoPin = 12;
int firePin = 0;
int fire = 1000;

void setup()
{
  Serial.begin(9600);
  pinMode(servoPin, OUTPUT);
}

void loop()
{
  Serial.println(analogRead(firePin));
  if(analogRead(firePin) < fire)
  {
    digitalWrite(servoPin, HIGH);
  }
  else
  { 
    digitalWrite(servoPin, LOW);
  }
  delay(1000);
 
}
