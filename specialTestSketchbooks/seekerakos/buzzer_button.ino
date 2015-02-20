int Pin = 8;
int led = 13;


void setup ()
{
  pinMode(Pin, INPUT);
  pinMode(led, OUTPUT);
  pinMode(4, OUTPUT);
}

void loop()
{
  if (digitalRead(Pin) == HIGH )
  {
    buzz(4, 2500, 500);
  }
  else
  {
    //nothing
  }
}
  void buzz(int targetPin, long frequency, long length) {
  long delayValue = 1000000/frequency/2; 
  long numCycles = frequency * length/ 1000; 
 for (long i=0; i < numCycles; i++){ 
    digitalWrite(targetPin,HIGH); 
    delayMicroseconds(delayValue); 
    digitalWrite(targetPin,LOW); 
    delayMicroseconds(delayValue); 
  }
}

