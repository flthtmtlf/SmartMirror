## Arduino Instruction : 아두이노 개요

 This is the schematic and source code of Arduino Board. The board like Arduino Leonardo can simulate mouse input, using ATmega32U4 Processer.

 아두이노의 배선도와 코드입니다.
 아두이노는 아두이노 레오나르도(Arduino Leonardo)와 같은 ATmega32U4 프로세서를 사용하는 아두이노 보드를 이용하여 마우스 입력을 시뮬레이팅 합니다.

## Files : 파일 설명

 - ultrasonic_mirror.ino: The code to control mouse input depending on the value of length, which is measured by ultrasonic sensor. requires Mouse.h library to compile and upload.<br>
 초음파 센서에서 받아들인 정보를 바탕으로 마우스를 조작하는 아두이노 코드입니다. Mouse.h 라이브러리를 필요로 합니다.

 <img src=schematic.PNG>

 - schematic.PNG: the way to connect arduino with ultrasonic sensor.<br>
 아두이노와 초음파 센서를 연결하는 방법입니다.
