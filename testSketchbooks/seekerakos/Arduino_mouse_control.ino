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
   val = results.value;
   Serial.println(val);
    irrecv.resume(); // Receive the next value
  }
}
