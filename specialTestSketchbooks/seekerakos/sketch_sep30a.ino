
int e1=10;
int e11=11;
int e2=8;
int e21=6;
int e3=13;
int e31=12;

void setup()
{
  pinMode(1,OUTPUT);
  pinMode(e1,OUTPUT);
  pinMode(e11,OUTPUT);
  pinMode(e2,OUTPUT);
  pinMode(e21,OUTPUT);
  pinMode(e3,OUTPUT);
  pinMode(e31,OUTPUT);
}

void loop()
{
 
  digitalWrite(e1,HIGH);
  digitalWrite(e11,HIGH);
  digitalWrite(e2,HIGH);
  digitalWrite(e21,HIGH);
  digitalWrite(e3,HIGH);
  digitalWrite(e31,HIGH);
  delay(2000);
  
  digitalWrite(e1,LOW);
  digitalWrite(e11,LOW);
  digitalWrite(e2,LOW);
  digitalWrite(e21,LOW);
  digitalWrite(e3,LOW);
  digitalWrite(e31,LOW);
  delay(2000);
}
