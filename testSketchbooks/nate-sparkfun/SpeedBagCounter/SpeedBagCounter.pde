/* 
 MMA8452Q Basic Example Code
 Nathan Seidle
 SparkFun Electronics
 2/23/2013
 
 License: This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).
 
 This example code shows how to read the X/Y/Z accelerations and basic functions of the MMA5842. It leaves out
 all the neat features this IC is capable of (tap, orientation, and inerrupts) and just displays X/Y/Z. See 
 the advanced example code to see more features.
 
 Hardware setup:
 MMA8452 Breakout ------------ Arduino
 3.3V --------------------- 3.3V
 SDA -------^^(330)^^------- A4
 SCL -------^^(330)^^------- A5
 GND ---------------------- GND
 
 The MMA8452 is 3.3V so we recommend using 330 or 1k resistors between a 5V Arduino and the MMA8452 breakout.
 
 The MMA8452 has built in pull-up resistors for I2C so you do not need additional pull-ups.
 
 Notes:
 
 3/2/2013 - got data from Hugo and myself, 3 rounds, on 2g setting. Very noisy but mostly worked
 
 */

#include <avr/wdt.h> //We need watch dog for this program

#include <Wire.h> // Used for I2C

#include <SoftwareSerial.h> //Used to print to 7segment display
SoftwareSerial Serial7Segment(8, 7); //RX pin, TX pin

//#include <SD.h> //Used for data logging
//File myFile;

int hitCounter = 0; //Keeps track of the number of hits

const int resetButton = 6; //Button that resets the display and counter
const int LED = 13; //Status LED on D3

long lastPrint; //Used for printing updates every second

//Used in the new algorithm
float lastMagnitude = 0;
float lastFirstPass = 0;
float lastSecondPass = 0;
float lastThirdPass = 0;
long lastHitTime = 0;
int secondsCounter = 0;

//This was found using a spreadsheet to view raw data and filter it
const float WEIGHT = 0.9;

//This was found using a spreadsheet to view raw data and filter it
const int MIN_MAGNITUDE_THRESHOLD = 1000; //350 is good

//This is the minimum number of ms between possible hits
//We use this to filter out peaks that are too close together
const int MIN_TIME_BETWEEN_HITS = 90; //100 works well

void setup()
{
  wdt_reset(); //Pet the dog
  wdt_disable(); //We don't want the watchdog during init

  pinMode(resetButton, INPUT_PULLUP);
  pinMode(LED, OUTPUT);

  //pinMode(4, OUTPUT); //Cathode of LED
  //digitalWrite(4, LOW);
  
  Serial.begin(115200);
  Serial.println("Speed Bag Counter");

  initDisplay();
  
  initMMA8452(); //Test and intialize the MMA8452
  
  //startSD(); //Init the SD card and create a log file

  lastPrint = millis();
  lastHitTime = millis();

  wdt_enable(WDTO_250MS); //Unleash the beast
}

long loopCounter = 0;

void loop()
{
  wdt_reset(); //Pet the dog

  loopCounter++;
  
  if(millis() - lastPrint > 1000)
  {
    if(digitalRead(LED) == LOW)
      digitalWrite(LED, HIGH);
    else
      digitalWrite(LED, LOW);
    
    Serial.print("Loops: ");
    Serial.println(loopCounter);
    loopCounter = 0;
    
    lastPrint = millis();
    
    /*secondsCounter++;
    if(secondsCounter > 4)
    {
      myFile.flush(); //Record this file to SD
      secondsCounter = 0;

      Serial.println("File flush");
    }*/
  }
  
  //Check the accelerometer
  float currentMagnitude = getAccelData();

  //Send this value through four (yes four) high pass filters
  float firstPass = currentMagnitude - (lastMagnitude * WEIGHT) - (currentMagnitude * (1 - WEIGHT));
  lastMagnitude = currentMagnitude; //Remember this for next time around
  
  float secondPass = firstPass - (lastFirstPass * WEIGHT) - (firstPass * (1 - WEIGHT));
  lastFirstPass = firstPass; //Remember this for next time around

  float thirdPass = secondPass - (lastSecondPass * WEIGHT) - (secondPass * (1 - WEIGHT));
  lastSecondPass = secondPass; //Remember this for next time around

  float fourthPass = thirdPass - (lastThirdPass * WEIGHT) - (thirdPass * (1 - WEIGHT));
  lastThirdPass = thirdPass; //Remember this for next time around
  //End high pass filtering

  fourthPass = abs(fourthPass); //Get the absolute value of this heavily filtered value

  /*myFile.print(millis()/1000.0, 3);
  myFile.print(",");
  myFile.print(currentMagnitude);
  myFile.print(",");
  myFile.print(firstPass);
  myFile.print(",");
  myFile.print(secondPass);
  myFile.print(",");
  myFile.print(thirdPass);
  myFile.print(",");
  myFile.print(fourthPass);
  myFile.print(",");
  myFile.print(hitCounter);
  myFile.print(",");*/

  //See if this magnitude is large enough to care
  if(fourthPass > MIN_MAGNITUDE_THRESHOLD)
  { 
    //We have a potential hit!
      
    if(millis() - lastHitTime > MIN_TIME_BETWEEN_HITS)
    { 
      //We really do have a hit!
      hitCounter++;
      
      lastHitTime = millis();
      
      Serial.print("Hit: ");
      Serial.println(hitCounter);
    
      printHits(); //Updates the display
    }
  }

  
  //Check if we need to reset the counter and display
  if(digitalRead(resetButton) == LOW)
  {
    Serial.println("Reset!");
    
    //This breaks the file up so we can see where we hit the reset button
    /*myFile.println();
    myFile.println();
    myFile.print("Reset!");
    myFile.println();
    myFile.println();*/
    
    hitCounter = 0;

    Serial7Segment.write('v'); //Reset the display - this forces the cursor to return to the beginning of the display
    printHits(); //Updates the display

    while(digitalRead(resetButton) == LOW) wdt_reset(); //Pet the dog while we wait for you to remove finger

    //Do nothing for 1 second after you press the button, a sort of debounce
    for(int x = 0 ; x < 10 ; x++)
    {
      wdt_reset(); //Pet the dog
      delay(100);
    }
  }
  
  //myFile.println();
  
  //delay(10); //Use only if we have debug serial.print statements
}

/*void startSD()
{
  Serial.print("Initializing SD card...");
  // On the Ethernet Shield, CS is pin 4. It's set as an output by default.
  // Note that even if it's not used as the CS pin, the hardware SS pin 
  // (10 on most Arduino boards, 53 on the Mega) must be left as an output 
  // or the SD library functions will not work. 
   pinMode(10, OUTPUT);
   
  if (!SD.begin(8)) {
    Serial.println("initialization failed!");
    return;
  }
  Serial.println("initialization done.");

  char fileName[20];
  int x;
  for(x = 0 ; x < 255 ; x++)
  {
    sprintf(fileName, "baglog%d.txt", x);
    if(!SD.exists(fileName)) break;
  }
  if(x == 255){
    Serial.print("File error");
    while(1);
  }

  myFile = SD.open(fileName, FILE_WRITE);

  if (myFile){
    Serial.print(fileName);
    Serial.println(" is now open");
  }
  else 
  {
    Serial.println("error opening file");
    while(1);
  }
  
}*/

//To shave some cycles we communicate at 57600bps
//This function makes sure the display is at 57600
void initDisplay()
{
  //Step through each baud rate and send the command to go to 57600
  int baudRates[7] = {2400, 4800, 9600, 14400, 19200, 38400, 115200};
  for (int i = 0 ; i < 7 ; i++)
  {
    Serial7Segment.begin(baudRates[i]);  // Set new baud rate
    delay(10);  // Arduino needs a moment to setup serial

    Serial7Segment.write(0x7F);  // Baud rate control command
    Serial7Segment.write(6);  // Let's go to 57600bps  
  }

  Serial7Segment.begin(57600);  // Set new baud rate
  Serial7Segment.write('v'); //Reset the display - this forces the cursor to return to the beginning of the display
  printHits(); //Update display with current hit count
}

//Uses sprintf to push the current hit counter to the display
void printHits(){
  char tempString[10];
  //sprintf(tempString, "%04d", hitCounter);
  sprintf(tempString, "%04d", hitCounter/2); //Cut in half
  Serial7Segment.print(tempString); //Send serial string out the soft serial port to the S7S
}
