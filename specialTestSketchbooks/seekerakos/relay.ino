int LightPin = 0;
int Light = 900;
int relay=8;
void setup()
{
  Serial.begin(9600);
  pinMode(8, OUTPUT);
}

void loop()
{
  Serial.println(analogRead(LightPin));
  if(analogRead(LightPin) >Light)
  {
    digitalWrite(relay, HIGH);
  }
  else
  { 
    digitalWrite(relay, LOW);
  }
  delay(1000);
}
