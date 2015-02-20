int firePin = 0;
int fire = 200;
void setup()
{
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop()
{
  Serial.println(analogRead(firePin));
  if(analogRead(firePin) < fire)
  {
    digitalWrite(13, HIGH);
  }
  else
  { 
    digitalWrite(13, LOW);
  }
  delay(1000);
}
