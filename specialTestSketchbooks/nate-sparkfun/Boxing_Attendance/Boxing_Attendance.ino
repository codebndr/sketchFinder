/* 
 RFID Attendance at a Boxing Gym
 By: Nathan Seidle
 SparkFun Electronics
 Date: January 9th, 2014
 License: This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).
 
 RFID sends us 12 hex characters at 9600bps
 Examples:
 670073E09F6B
 5900108C2AEF
 
 */
#include <avr/wdt.h> //We need the watch dog for this program

#include <SoftwareSerial.h> //Connection to Imp

SoftwareSerial rfid(2, 3); // RX from ID-20LA, TX not connected
SoftwareSerial imp(8, 9); // RX, TX into Imp pin 7

//Hardware pin definitions
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
// digital I/O pins
const int REDLED = 4;
const int GREENLED = 5;
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

//Global Variables
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
char id[13]; //An ID should be 12 bytes plus a termination char \0

boolean sentToCloud = false; //Boolean to keep track of if we should we watching the clock
long sentTime; //This is the moment in time that we sent the ID to the cloud. Timeout if necessary
//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


void setup()
{
  wdt_reset(); //Pet the dog
  wdt_disable(); //We don't want the watchdog during init

  Serial.begin(19200);
  imp.begin(19200);
  rfid.begin(9600);

  pinMode(REDLED, OUTPUT);
  pinMode(GREENLED, OUTPUT);
  blinkError();

  Serial.println("Boxing attendant online!");
  
  rfid.listen(); //We can only listen to one software serial port at a time

  wdt_enable(WDTO_4S); //Unleash the beast
}

void loop()
{
  wdt_reset(); //Pet the dog

  //If we see an RFID card, push it to imp then make noise and light LED
  if(rfid.available())
  {
    //If the ID is clean, report it. If it's corrupt, just debug print the info.
    if(readCleanID() == true)
    {
      Serial.print("Read: ");
      Serial.println(id);

      imp.print("$,");
      imp.print(id);
      imp.println(",#,"); //Imp looks for terminating new line
      imp.listen(); //We can only listen to one software serial port at a time
      
      sentToCloud = true;
      sentTime = millis(); //Record the moment in time we reported the ID
    }
    else
    {
      Serial.print("Read error: ");
      Serial.println(id);
      
      blinkError();
      
      if(rfid.available() > 0)
      {
        int counter = 0;
        while(rfid.available())
        {
          rfid.read(); //Remove any characters in buffer
          counter++;
          if(counter > 20) break; //Give up after 100ms
          delay(5);
        }
      }
    }
  }
  
  //The imp should respond with a good/bad character
  if(imp.available())
  {
    byte incoming = imp.read();
    if(incoming == 'G')
    {
      Serial.println("You are allowed!");
      blinkGood(); //Blink green LED
    }
    else if(incoming == 'B')
    {
      Serial.println("BAD! Go away!");
      blinkBad(); //Blink red LED
    }

    rfid.listen(); //Whatever the imp said, return to listening to the RFID reader
    sentToCloud = false; //Reset and wait for next thing to send to cloud
  }

  //If the electric imp fails to be able to connect to the internet
  //then we could get stuck infinitely waiting for a response from the cloud.
  //This block of code protects us from that.
  if(sentToCloud == true && millis() - sentTime > 3000)
  {
    //Give up and display error
    blinkError();
    sentToCloud = false;
    rfid.listen(); //Return to listening to RFID reader
  }

  delay(10);
}

//Blinks the green and red LEDs in unison indicating read error
void blinkError(void)
{
  for(int x = 0 ; x < 12 ; x++)
  {
    digitalWrite(REDLED, HIGH);
    digitalWrite(GREENLED, LOW);
    delay(25);
    digitalWrite(REDLED, LOW);
    digitalWrite(GREENLED, HIGH);
    delay(25);
  }
  digitalWrite(REDLED, LOW);
  digitalWrite(GREENLED, LOW);
  
  //Beep buzzer
}

void blinkGood(void)
{
  for(int x = 0 ; x < 1 ; x++)
  {
    digitalWrite(GREENLED, HIGH);
    delay(1000);
    digitalWrite(GREENLED, LOW);
    //delay(100);
  }
}

void blinkBad(void)
{
  for(int x = 0 ; x < 1 ; x++)
  {
    digitalWrite(REDLED, HIGH);
    delay(1000);
    digitalWrite(REDLED, LOW);
    //delay(500);
  }
}

//The ID should be 12 characters long but there's often noise
//This tries to eliminate as much as possible
//Returns true if a good ID is found
boolean readCleanID(void)
{
  char incoming[30]; //Make this buffer a little big, just in case
  char outgoing[30]; //This array holds all the viable chars

  byte charsRead;
  byte counter = 0;
  while(counter < 30)
  {
    if(rfid.available())
    {
      incoming[charsRead] = rfid.read(); //Gather everthing in the buffer
      charsRead++;
      if(charsRead == 14) break; //12 chars plus a \n
    }
    counter++;
    delay(1);
  }

  //Clean the buffer
  int sizeOfString = 0;
  int spot = 0;
  for(int x = 0 ; x < charsRead ; x++)
  {
    if(incoming[x] >= '0' && incoming[x] <= 'F') //The RFID reader only reports hex characters
    {
      //We have a valid character, record it!
      outgoing[spot++] = incoming[x];
      sizeOfString++; //Record the increased size
      if(sizeOfString > 20) break; //Bail if there is more than 20. We are looking for 12.
    } 
  }
  
  if(sizeOfString != 12)
  {
    //Serial.println("Bad read");

    //Push the current read to the ID array for debugging
    for(int x = 0 ; x < 12 ; x++)
      id[x] = outgoing[x];
    id[12] = '\0'; //Termination character for the actual string we pass to the cloud

    return(false); //We couldn't clean this string to make it good enough
  }
  else
  {
    //Record this good array into the passable ID array
    for(int x = 0 ; x < 12 ; x++)
      id[x] = outgoing[x];
    id[12] = '\0'; //Termination character for the actual string we pass to the cloud

    return(true); //ID is good to pass to the cloud
  }
}

