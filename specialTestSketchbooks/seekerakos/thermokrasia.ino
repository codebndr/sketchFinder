int cel = 0;
int temp;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
  temp = (5.0 * analogRead(cel) * 100.0) / 1024.0;
  Serial.print(temp);
  Serial.println("  Vathmous kelsiou");
  temp =0;
  delay(3000);
}

