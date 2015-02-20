#include <VirtualWire.h>

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
  
  char msg[4];
  int val;
  val=analogRead(0);
  itoa(val,msg,10);
     
    vw_send((uint8_t *)msg, strlen(msg));
    vw_wait_tx(); // Wait until the whole message is gone
    Serial.println(msg);
 
  }
