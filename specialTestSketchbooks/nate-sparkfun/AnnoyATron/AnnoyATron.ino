/*
 1-14-2013
 Spark Fun Electronics
 Nathan Seidle
 
 This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).
 
 When the ATtiny senses water between two pins, go crazy. Make noise, blink LED.
 
 
 */

#include <avr/sleep.h> //Needed for sleep_mode
#include <avr/wdt.h> //Needed to enable/disable watch dog timer

//Pin definitions for ATtiny
const byte buzzer1 = 0;
const byte buzzer2 = 1;

volatile unsigned int watchdog_counter = 0;
unsigned int randomAmount;

//This runs each time the watch dog wakes us up from sleep
ISR(WDT_vect) {
  //Don't do anything. This is just here so that we wake up.
  watchdog_counter++;
}

void setup()
{
  pinMode(buzzer1, OUTPUT);
  pinMode(buzzer2, OUTPUT);
  
  randomSeed(analogRead(A1));
  randomAmount = random(60, 120); //Give me a random amount of time between beeps

  //During power up, beep the buzzer to verify function
  alarmSound();
  delay(100);
  alarmSound();

  digitalWrite(buzzer1, LOW);
  digitalWrite(buzzer2, LOW);

  //Power down various bits of hardware to lower power usage  
  set_sleep_mode(SLEEP_MODE_PWR_DOWN); //Power down everything, wake up from WDT
  sleep_enable();
  ADCSRA &= ~(1<<ADEN); //Disable ADC, saves ~230uA
}

void loop() 
{
  setup_watchdog(6); //Setup watchdog to go off after 1sec
  sleep_mode(); //Go to sleep! Wake up 1sec later and check water

  if(watchdog_counter > randomAmount)
  {
    //Beep!
    alarmSound();
    randomAmount = random(60, 120); //Give me a random amount of time between beeps
    watchdog_counter = 0;

    digitalWrite(buzzer1, LOW);
    digitalWrite(buzzer2, LOW);
  }
}

//This is just a unique (annoying) sound we came up with, there is no magic to it
//Comes from the Simon Says game/kit actually: https://www.sparkfun.com/products/10547
//250us to 79us
void alarmSound(void)
{
  // Toggle the buzzer at various speeds
  for (byte x = 250 ; x > 70 ; x--)
  {
    for (byte y = 0 ; y < 3 ; y++)
    {
      digitalWrite(buzzer2, HIGH);
      digitalWrite(buzzer1, LOW);
      delayMicroseconds(x);

      digitalWrite(buzzer2, LOW);
      digitalWrite(buzzer1, HIGH);
      delayMicroseconds(x);
    }
  }
}

//Sets the watchdog timer to wake us up, but not reset
//0=16ms, 1=32ms, 2=64ms, 3=128ms, 4=250ms, 5=500ms
//6=1sec, 7=2sec, 8=4sec, 9=8sec
//From: http://interface.khm.de/index.php/lab/experiments/sleep_watchdog_battery/
void setup_watchdog(int timerPrescaler) {

  if (timerPrescaler > 9 ) timerPrescaler = 9; //Limit incoming amount to legal settings

  byte bb = timerPrescaler & 7; 
  if (timerPrescaler > 7) bb |= (1<<5); //Set the special 5th bit if necessary

  //This order of commands is important and cannot be combined
  MCUSR &= ~(1<<WDRF); //Clear the watch dog reset
  WDTCR |= (1<<WDCE) | (1<<WDE); //Set WD_change enable, set WD enable
  WDTCR = bb; //Set new watchdog timeout value
  WDTCR |= _BV(WDIE); //Set the interrupt enable, this will keep unit from resetting after each int
}
