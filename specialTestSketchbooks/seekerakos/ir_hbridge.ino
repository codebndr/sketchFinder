#include <IRremote.h>
int RECV_PIN = 5;
IRrecv irrecv(RECV_PIN);
decode_results results;
int val;

int motor1Pin = 2;    // H-bridge leg 1 (pin 2, 1A)
int motor2Pin = 3;    // H-bridge leg 2 (pin 7, 2A)
int enablePin = 4;    // H-bridge enable pin
int ledPin = 13;      // LED 

  void setup() {
    // set the switch as an input:
      Serial.begin(9600);
  irrecv.enableIRIn();
     

    // set all the other pins you're using as outputs:
    pinMode(motor1Pin, OUTPUT); 
    pinMode(motor2Pin, OUTPUT); 
    pinMode(enablePin, OUTPUT);
    pinMode(ledPin, OUTPUT);
  }

  void loop() {
    if (irrecv.decode(&results)) {
    
   val= results.value;
   Serial.println(val);
    irrecv.resume();
    // if the switch is high, motor will turn on one direction:
    if (val == 22950 ) {
      pinMode(enablePin,HIGH);
    } 
    // if the switch is low, motor will turn in the other direction:
    else {
      pinMode(enablePin,LOW);
    }
  }
  if (val == 16830){
    digitalWrite(motor1Pin, LOW);   // set leg 1 of the H-bridge low
      digitalWrite(motor2Pin, HIGH);  // set leg 2 of the H-bridge high
  }
   else {
      digitalWrite(motor1Pin, HIGH);  // set leg 1 of the H-bridge high
      digitalWrite(motor2Pin, LOW);   // set leg 2 of the H-bridge low
    }
  }
