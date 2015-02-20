#include <VirtualWire.h>
int button = 8;
void setup()
{
    Serial.begin(9600);	  // Debugging only
    Serial.println("setup");
    pinMode(button,INPUT);
    // Initialise the IO and ISR
    vw_set_ptt_inverted(true); // Required for DR3100
    vw_setup(2000);	 // Bits per sec
        vw_set_tx_pin(3); 
        
     

}

void loop()
{
  char *msg;
  
  if (digitalRead(button))== HIGH){
    msg = "1";
      digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);
    delay(5000);
    msg = "0";
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx();
    delay(5000);
  }
    
}

      
  
