#include <VirtualWire.h>
void one();
void two();
void setup()
{
    Serial.begin(9600);	  // Debugging only

    // Initialise the IO and ISR
    vw_set_ptt_inverted(true); // Required for DR3100
    vw_setup(2000);	 // Bits per sec
        vw_set_tx_pin(3); 
       

}

void loop()
{
    one();
    
    
    delay(5000);
    two();
    delay(5000);
  }
  
  void one()
  {
  //char *msg;
    char *msg = "4";
    digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);
  
  }
  
  void two(){
  //char *msg;
    char *msg = "2";
    digitalWrite(13, true); // Flash a light to show transmitting
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    digitalWrite(13, false);
  
  }
