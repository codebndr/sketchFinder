int vert = 1 ;
int horz = 0;


void setup()
{
  Serial.begin(9600);
}
void loop()
{
  Serial.print(analogRead(horz));
  Serial.print(",");
  Serial.println(analogRead(vert));
  delay(1000);
}
