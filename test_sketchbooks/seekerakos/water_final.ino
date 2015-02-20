int water = 0;
int eksodos;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
  
  
  eksodos = (analogRead(water)/425.0)*100.0;
  Serial.println(eksodos);
  eksodos = 0;
  delay(3000);
}
