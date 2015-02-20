int ledPin = 13;

void setup()
{
  pinMode(ledPin, OUTPUT);
}

void loop()
{
  digitalWrite(ledPin, LOW);
  delay(2000);
  digitalWrite(ledPin, LOW);
  delay(100);
}
