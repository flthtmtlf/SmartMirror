#include "Mouse.h"


int trigPin = 11;
int echoPin = 12;
long duration, cm;
int MOE = 2;
int counter = 0;
bool IsNear = false;
bool last = false;
bool over = true;


void setup() {
  Serial.begin (9600);
  Mouse.begin();
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

void loop() {

  digitalWrite(trigPin, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  pinMode(echoPin, INPUT);
  duration = pulseIn(echoPin, HIGH);


  cm = (duration / 2) / 29.1;


  if (cm >= 50)
  {
    IsNear = false;
  }

  else if (cm <= 50)
  {
    IsNear = true;
  }

  //=========Rising Edge Detection ==============

  if (IsNear != last)
  {
    if (IsNear == true)
    {
      over = true;
    }
  }


  //=========Falling Edge Detection ==============

  if (IsNear != last)
  {
    if (IsNear == false)
    {
      over = true;
    }
  }
  //========= MOE = 2 =============
  if (IsNear == last)
  {
    if (over == true)
    {
      counter ++;
    }
  }


  //======= OUTPUT 1 =======
  if (counter == MOE)
  {
    if (IsNear == true) {

      Mouse.move(100, 0);
      delay(5);
      Mouse.move(100, 0);
      delay(5);
      Mouse.move(100, 0);
      delay(10);
      Mouse.click(MOUSE_LEFT);

      counter = 0;
      over = false;
    }
  }
  
  //======= OUTPUT 2 =======

  if (counter == MOE)
  {
    if (IsNear == false) {

      Mouse.move(-100, 0);
      delay(5);
      Mouse.move(-100, 0);
      delay(5);
      Mouse.move(-100, 0);
      delay(10);
      Mouse.click(MOUSE_LEFT);

      counter = 0;
      over = false;
    }
  }


  last = IsNear;

  delay(250);
}
