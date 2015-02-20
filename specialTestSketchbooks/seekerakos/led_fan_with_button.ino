int ledPin = 8;
int led = 10;
int pin = 11;
int but = 7;

void setup() 
{
  pinMode(ledPin, OUTPUT); 
  pinMode(led, OUTPUT);
  pinMode(pin, OUTPUT);  
  pinMode(but, INPUT);
}

void loop()
{
  if (digitalRead(but)==HIGH) 
  {
  digitalWrite(ledPin, HIGH); 
  delay(1000); 
  digitalWrite(ledPin, LOW); 
  delay(500);
  digitalWrite(led,HIGH);
  delay(1000);
  digitalWrite(led, LOW); 
  delay(500);
  digitalWrite(11,HIGH);
  delay(1000);
  digitalWrite(11, LOW);
  delay(500);
  digitalWrite(8,HIGH);
  digitalWrite(10,HIGH);
  digitalWrite(11,HIGH);
  delay(2000);
  digitalWrite(8,LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  delay(1000);
  }
  else
  {
  digitalWrite(ledPin, HIGH); 
  delay(200); 
  digitalWrite(ledPin, LOW); 
  delay(100);
  digitalWrite(led,HIGH);
  delay(200);
  digitalWrite(led, LOW); 
  delay(100);
  digitalWrite(11,HIGH);
  delay(200);
  digitalWrite(11, LOW);
  delay(100);
  }
}


