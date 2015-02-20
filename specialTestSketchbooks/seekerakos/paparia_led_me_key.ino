int red =13;
int blue =12 ;
int green = 11;
int swit = 7;

void setup()
{
  pinMode(red,OUTPUT);
  pinMode(blue,OUTPUT);
  pinMode(green,OUTPUT);
  pinMode(swit,INPUT);
}

void loop()
{
 
  if (digitalRead(swit) == HIGH) {
    
      digitalWrite(red,HIGH);}
    else
    {
      digitalWrite(red,LOW);
    }
}

