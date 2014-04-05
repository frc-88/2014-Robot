#include "FastLED.h"

//Tell it how many leds are in the strip. AndyMark's 2.5 meter strip has 80 leds
#define NUM_LEDSUp 21
#define NUM_LEDSARM 22
#define NUM_LEDSUNDER 79
#define NUM_LEDSUpR 20
//21
// This is an array of leds. One item for each led in your strip.
CRGB leds[NUM_LEDSUp];

CRGB leds2[NUM_LEDSUpR];
CRGB leds3[NUM_LEDSARM];
CRGB leds4[NUM_LEDSUNDER];

//CSK 3/17/2014 I moved these to pins that don't conflict with Ethernet functions in case you want to control LEDs via Ethernet
#define DATA_PIN 7 //Green wire from AM-2640's power connector
// Clock pin SPI
#define CLOCK_PIN 6 //Blue wire from AM-2640's power connector
#define DATA_PIN2 5
#define CLOCK_PIN2 4
#define DATA_PIN3 3
#define CLOCK_PIN3 2
#define DATA_PIN4 9
#define CLOCK_PIN4 8

#define MAX_BRIGHTNESS 255
int count = 0;
int blue = 0;
//This function is used to setup things like pins, Serial ports etc.
//Here we specify which chipset our LEDs run off of by our choice of config function
void setup()
{
	// Uncomment one of the following lines for your leds arrangement.
	// FastLED.addLeds<TM1803, DATA_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<TM1804, DATA_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<TM1809, DATA_PIN, RGB>(leds, NUM_LEDS);
	//FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<WS2812, DATA_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<WS2812B, DATA_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<UCS1903, DATA_PIN, RGB>(leds, NUM_LEDS);

	//FastLED.addLeds<WS2801, RGB>(leds, NUM_LEDS);

	// FastLED.addLeds<SM16716, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<LPD8806, RGB>(leds, NUM_LEDS);

	//***This is the chipset in the AM-2640 LED strip***
	//CSK 3/17/2013 Changed to this function to allow direct data and clock pin specification
	FastLED.addLeds<WS2801, DATA_PIN, CLOCK_PIN, RGB>(leds3, NUM_LEDSARM);
	FastLED.addLeds<WS2801, DATA_PIN4, CLOCK_PIN4, RGB>(leds2, NUM_LEDSUp);
	FastLED.addLeds<WS2801, DATA_PIN3, CLOCK_PIN3, RGB>(leds, NUM_LEDSUp);
	FastLED.addLeds<WS2801, DATA_PIN2, CLOCK_PIN2, RGB>(leds4, NUM_LEDSUNDER);

        Serial.begin(9600);
	// FastLED.addLeds<SM16716, DATA_PIN, CLOCK_PIN, RGB>(leds, NUM_LEDS);
	// FastLED.addLeds<LPD8806, DATA_PIN, CLOCK_PIN, RGB>(leds, NUM_LEDS);
}

void loop()
{
	//This is kind of Arduino's equivalent to Main() in a standard C program
	//This, as the name implies, loops endlessly.
	//https://code.google.com/p/fastspi/wiki/CRGBreference
	//CSK 3/20/2014 I added a rainbow function just for grins
//        Serial.println("rainbow");
//        fill(leds3, NUM_LEDSARM, 0, 255, 0);
//        delay(100);
//        fill(leds, NUM_LEDSUp, 0, 255, 0);
//        delay(100);
//        fill(leds2, NUM_LEDSUp, 0, 255, 0);
//        fill(leds4, NUM_LEDSUNDER, 0, 255, 0);
//        FastLED.show();
        Serial.println(count);
        if (count < 500) {
          tj(leds, NUM_LEDSUp);
          tj(leds2, NUM_LEDSUp);
          tj(leds3, NUM_LEDSARM);
          tj(leds4, NUM_LEDSUNDER);
        }else if ((count >= 500) && (count <1000)) {
          if (blue==1) {
            fill(leds3, NUM_LEDSARM, 0, 0, 255);
            fill(leds, NUM_LEDSUp, 0, 0, 255);
            fill(leds2, NUM_LEDSUp, 0, 0, 255);
            fill(leds4, NUM_LEDSUNDER, 0, 0, 255);  
          }else {  
            fill(leds3, NUM_LEDSARM, 255, 0, 0);
            fill(leds, NUM_LEDSUp, 255, 0, 0);
            fill(leds2, NUM_LEDSUp, 255, 0, 0);
            fill(leds4, NUM_LEDSUNDER, 255, 0, 0);
          }
        }else if ((count >=1000) && (count < 1100)) {
          fill(leds3, NUM_LEDSARM, 255, 0, 255);
          fill(leds, NUM_LEDSUp, 255, 0, 255);
          fill(leds2, NUM_LEDSUp, 255, 0, 255);
          fill(leds4, NUM_LEDSUNDER, 255, 0, 255);
        }else if ((count >=1100) && (count < 1200)) {
          fill(leds3, NUM_LEDSARM, 255, 255, 0);
          fill(leds, NUM_LEDSUp, 255, 255, 0);
          fill(leds2, NUM_LEDSUp, 255, 255, 0);
          fill(leds4, NUM_LEDSUNDER, 255, 255, 0); 
        } else if ((count >=1200) && (count < 1300)) {
          fill(leds3, NUM_LEDSARM, 255, 0, 255);
          fill(leds, NUM_LEDSUp, 255, 0, 255);
          fill(leds2, NUM_LEDSUp, 255, 0, 255);
          fill(leds4, NUM_LEDSUNDER, 255, 0, 255);
        }else if ((count >=1300) && (count < 1400)) {
          fill(leds3, NUM_LEDSARM, 255, 255, 0);
          fill(leds, NUM_LEDSUp, 255, 255, 0);
          fill(leds2, NUM_LEDSUp, 255, 255, 0);
          fill(leds4, NUM_LEDSUNDER, 255, 255, 0); 
        } 
        else if (count>=1400 && count <1500) {
          if (blue==1) {
          fill(leds3, NUM_LEDSARM, 0, 0, 255);
          fill(leds, NUM_LEDSUp, 0, 0, 255);
          fill(leds2, NUM_LEDSUp, 0, 0, 255);
          fill(leds4, NUM_LEDSUNDER, 0, 0, 255);  
          }else {  
          fill(leds3, NUM_LEDSARM, 255, 0, 0);
          fill(leds, NUM_LEDSUp, 255, 0, 0);
          fill(leds2, NUM_LEDSUp, 255, 0, 0);
          fill(leds4, NUM_LEDSUNDER, 255, 0, 0);
          }
        }
        else  {
          rainbow(20, NUM_LEDSUNDER, leds4);
          color_chase(CRGB::Yellow, 20,5, NUM_LEDSUp, leds);
          delay(10);
          color_chase(CRGB::Yellow, 20,5, NUM_LEDSUpR, leds2);
          tj(leds3, NUM_LEDSARM);
          count=0;
        }
        count++;
        FastLED.show();
        delay(10);
//        FastLED.show();
//        rainbow(20, NUM_LEDSUp, leds);
//        rainbow(20, NUM_LEDSUp, leds2);
//        rainbow(20, NUM_LEDSARM, leds3);
//        rainbow(20, NUM_LEDSUNDER, leds4);

//        cylon(CRGB::Red,25, 4, NUM_LEDSUp, leds);
//        cylon(CRGB::Red,25, 4, NUM_LEDSUp, leds2);
//        cylon(CRGB::Red,25, 4, NUM_LEDSARM, leds3);
//        cylon(CRGB::Red,25, 4, NUM_LEDSUNDER, leds4);
//        if (count <=100) {
//          fill(leds, NUM_LEDSUp, 128,0,128);
//          fill(leds2, NUM_LEDSUp, 128,0,128);
//          fill(leds3, NUM_LEDSARM, 128,0,128);
//          fill(leds4, NUM_LEDSUNDER, 128,0,128);        
//        } else {
//          fill(leds, NUM_LEDSUp, 255,255,0);
//          fill(leds2, NUM_LEDSUp, 255,255,0);
//          fill(leds3, NUM_LEDSARM, 255,255,0);
//          fill(leds4, NUM_LEDSUNDER, 255,255,0); 
//          count = 0;
//          FastLED.show();
//          delay(1000);
//        }
//        delay(5);
//        count++;
//        FastLED.show();

        
        
//	Serial.println("Cyclone");
//        cylon(CRGB::Red,25, 4);
//	Serial.println("Green Chase");
//        color_chase(CRGB::Green, 10,5);
//        Serial.println("BlueVioletChase");
//	color_chase(CRGB::BlueViolet, 25,1);
//        Serial.println("RedChase");
//        color_chase(CRGB::Red, 1,5);
//        Serial.println("Yellow Chase");
//	color_chase(CRGB::Yellow, 25,1);
//        Serial.println("Green Chase");
//	color_chase(CRGB::Green, 25,1);
//        Serial.println("Cyan Chase");
//	color_chase(CRGB::Cyan, 25,1);
//        Serial.println("Blue Chase");
//	color_chase(CRGB::Blue, 25,1);
//        Serial.println("White Dot");
//	missing_dot_chase(CRGB::White, 20);
//        Serial.println("RED DOT");
//	missing_dot_chase(CRGB::Red, 150);
//        Serial.println("yELLOW DOT");
//	missing_dot_chase(CRGB::Yellow, 20);
//        Serial.println("GREEN DOT");
//	missing_dot_chase(CRGB::Green, 20);
//        Serial.println("cYAN DOT");
//	missing_dot_chase(CRGB::Cyan, 20);
//        Serial.println("DOT BLUE");
//	missing_dot_chase(CRGB::Blue, 20);
//        Serial.println("CHIP");
//	missing_dot_chase(0x3000cc, 20) ;
//tj(leds,NUM_LEDS);
//delay(10);
//tj(leds2,NUM_LEDS);
//fill(leds, 21, 0,255,0);
//delay(10);
//tj(leds3,NUM_LEDS);

//FastLED.show();
//delay(4000);
}

//These are the functions we have defined to do chase patterns.  They are actually called inside the  loop() above
//They are meant to demonstrate things such as setting LED colors, controlling brightness
void color_chase(uint32_t color, uint8_t wait, int chaseLength, int NUM_LEDS, CRGB* strip)
{
	//clear() turns all LEDs off
	FastLED.clear();
	//The brightness ranges from 0-255
	//Sets brightness for all LEDS at once
	FastLED.setBrightness(MAX_BRIGHTNESS);
	// Move a single led
	for(int led_number = 0; led_number < NUM_LEDS; led_number++){
		// Turn our current led ON, then show the leds
                for (int length = 0; length < chaseLength; length++) {
                  int index = led_number + length;
                  if (index < NUM_LEDS) {
                    strip[index] = color;
                    }
                  Serial.println(index);
                  //leds[index].g = green;
                  //leds[index].b = blue;
                }


		// Show the leds (only one of which is has a color set, from above
		// Show turns actually turns on the LEDs
		FastLED.show();

		// Wait a little bit
		delay(wait);

		// Turn our current led back to black for the next loop around
              for (int length = 0; length < chaseLength; length++) {
                 int index = led_number + length;
                 if (index < NUM_LEDS) {
                  strip[index] = CRGB::Black;
	        } 
                  }
	}
	return;
}

//Move an "empty" dot down the strip
void missing_dot_chase(uint32_t color, uint8_t wait, int NUM_LEDS)
{
	//Step thru some brightness levels from max to 10.  led_brightness/=2 is a cryptic shorthand way of saying led_brightness = led_brightness/2
	for (int led_brightness = MAX_BRIGHTNESS; led_brightness > 10; led_brightness/=2)
	{
		FastLED.setBrightness(led_brightness);

		// Start by turning all pixels on:
		//for(int led_number = 0; led_number < NUM_LEDS; led_number++) leds[led_number] = color;
		//https://github.com/FastLED/FastLED/wiki/Controlling-leds
		fill_solid(leds, NUM_LEDS, color);

		// Then display one pixel at a time:
		for(int led_number = 0; led_number < NUM_LEDS; led_number++)
		{
			leds[led_number] =  CRGB::Black; // Set new pixel 'off'
			if( led_number > 0 && led_number < NUM_LEDS)
			{
				leds[led_number-1] = color; // Set previous pixel 'on'
			}
			FastLED.show();
			delay(wait);
		}
	}
	return;
}

//Cylon - LED sweeps back and forth, with the color, delay and number of cycles of your choice 
void cylon(CRGB color, uint16_t wait, uint8_t number_of_cycles, int NUM_LEDS, CRGB* strip)
{
	FastLED.setBrightness(255);
	for (uint8_t times = 0; times<=number_of_cycles; times++)
	{
		// Make it look like one LED is moving in one direction
		for(int led_number = 0; led_number < NUM_LEDS; led_number++)
		{
			//Apply the color that was passed into the function
			strip[led_number] = color;
			//Actually turn on the LED we just set
			FastLED.show();
			// Turn it back off
			strip[led_number] = CRGB::Black;
			// Pause before "going" to next LED
			delay(wait);
		}

		// Now "move" the LED the other direction
		for(int led_number = NUM_LEDS-1; led_number >= 0; led_number--)
		{
			//Apply the color that was passed into the function
			strip[led_number] = color;
			//Actually turn on the LED we just set
			FastLED.show();
			// Turn it back off
			strip[led_number] = CRGB::Black;
			// Pause before "going" to next LED
			delay(wait);
		}
	}
	return;
}

void clearPixels(int ledNum, CRGB* strip){
  for (int i = 0; i < ledNum; i++) {
    strip[i] = CRGB::Black;
    //kendall says -> is basically a . in java so reference to an object
   //can also do FastLED.clear(); but it clears all I think? 
  }
}

void rainbow(uint8_t wait, int NUM_LEDS, CRGB* strip) 
{

	uint16_t hue;
	clearPixels(NUM_LEDS, strip);

	for(hue=10; hue<255*3; hue++) 
	{
              
		fill_rainbow( &(strip[0]), NUM_LEDS /*led count*/, hue /*starting hue*/);		
		FastLED.show();
		delay(wait);
	}
	return;
}

void tj(CRGB* strip, int numLEDs) {
  uint32_t color = 0;
  for (int i = 0; i < numLEDs; i++) {
    int c = random(1, 8);
    // no purple
    if(c==5) {
      c = 4;
    }
    // if white, try again (i.e. low frequency of white)
    else if(c==7) {
      c = random(1, 8);
    }
    int r = 64  * (c>>2 & 1);
    int g = 64 * (c>>1 & 1);
    int b = 64 * (c & 1);
    strip[i].setRGB(r,g,b);
  }
}

// Fills the strip to a certain length of a solid color
void fill(CRGB* strip, int length, byte r , byte g , byte b) {
  //clearPixels(strip, length);
  //redundancy check. Taking it out 
//  // Limit the length of the fill to be the length of the strip
//  if (length > strip->numPixels()) {
//    length = strip->numPixels();
//  }

  // Set each pixel in the length to be the desired color
  for (int i = 0; i < length; i++) {
    strip[i].setRGB(r,g,b);
  }
}
