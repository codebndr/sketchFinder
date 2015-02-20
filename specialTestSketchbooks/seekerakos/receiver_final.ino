#include <VirtualWire.h>

void setup()
{
    Serial.begin(9600);	// Debugging only
   

    // Initialise the IO and ISR
    vw_set_ptt_inverted(true); // Required for DR3100
    vw_setup(2000);	 // Bits per sec
    vw_set_rx_pin(2);
    vw_rx_start();       // Start the receiver PLL running
    pinMode(13,OUTPUT);

}

void loop()
{
  
  
  
    uint8_t buf[VW_MAX_MESSAGE_LEN];
    uint8_t buflen = VW_MAX_MESSAGE_LEN;

    if (vw_get_message(buf, &buflen)) // Non-blocking
    {
	int i;

       
	
	
	for (i = 0; i < buflen; i++)
	{
	    Serial.print(buf[i]);
        if(buf[i] == '1'){digitalWrite(13, HIGH);}
       
        
	   
	}
	

   }
   delay(2000);
        digitalWrite(13, LOW);
}
