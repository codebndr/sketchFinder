/* TRANSMITTER CODE

#include <VirtualWire.h>

void setup()
{
    Serial.begin(9600);	  // Debugging only
    Serial.println("setup");

    // Initialise the IO and ISR
    vw_set_ptt_inverted(true); // Required for DR3100
    vw_setup(2000);	 // Bits per sec
        vw_set_tx_pin(3); 
        
        pinMode(8, INPUT);
        pinMode(9, INPUT);
        pinMode(10, INPUT);
        pinMode(11, INPUT);

digitalWrite(8, HIGH);
digitalWrite(9, HIGH);
digitalWrite(10, HIGH);
digitalWrite(11, HIGH);

}

void loop()
{
  char *msg;
  
  if(digitalRead(8) == LOW){
    char *msg = "1";
      digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);}
  if(digitalRead(9) == LOW){
    char *msg = "2";
      digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);}
  if(digitalRead(10) == LOW){
    char *msg = "3";
      digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);}
  if(digitalRead(11) == LOW){
    char *msg = "4";
      digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);}
  }
  
  END TRANSMITTER CODE */ 
