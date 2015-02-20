#include <IRremote.h>

int RECV_PIN = 11;
IRrecv irrecv(RECV_PIN);
decode_results results;
int val;


void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
 


}

void loop() {
  if (irrecv.decode(&results)) {
    Serial.println(results.value);
   val= results.value;
   Serial.println(val);
    irrecv.resume(); // Receive the next value
  }
if(val == 22950){
  digitalWrite(13,HIGH);}
  if(val==16830){
    digitalWrite(13,LOW);}
}
