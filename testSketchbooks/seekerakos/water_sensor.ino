int waterPin = 0;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
 Serial.println(analogRead(waterPin));
delay(1000); 
}
