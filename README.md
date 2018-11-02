# SmartMirror : 스마트 미러 프로젝트

## Introduction : 개요

 This is Smart Mirror Project Repository.

 스마트 미러 프로젝트입니다!

## Things you should know : 주의사항

 In this repo, there is 2 branches:

 - master branch : main branch. it will be updated at alpha release.
 - black branch : working branch. everything will be commited, tested,<br>
  applied or declined in this branch.

 본 리포지터리에는 두 개의 브런치가 존재합니다.

 - master 브런치 : 주 브런치입니다. 릴리스 버전 업데이트 때마다 갱신됩니다.
 - black 브런치 : 작업 브런치입니다. 베타 버전의 소스 코드는 여기 커밋되어<br>
 테스트를 거치고 적용되거나 제거됩니다.

## How to set and use : 구성 방법

 1. Prepare the device like ODROID, which Android OS is installed on.<br>
 ODROID 등 Android OS가 설치된 기기를 준비합니다.
 2. Connect the device to monitor.<br>
 기기를 모니터와 연결합니다.
 3. Set magic mirror on the monitor.<br>
 모니터 화면에 반거울을 설치합니다.
 4. Prepare application file.<br>
 어플리케이션 파일을 준비합니다.
   - Build the application file using the source code of this repo.<br>
   본 리포지터리의 소스 코드를 이용하여 어플리케이션을 빌드합니다.<br>
   or<br>
   또는<br>
   - Download the application file from the link of this instruction's bottom.<br>
   하단 링크에서 어플리케이션 파일을 다운로드할 수 있습니다.
 5. Install the app file on the device and run it.<br>
 어플리케이션 파일을 기기에 설치하고 실행합니다.
 6. Prepare the device like Arduino Leonardo, which is programmable and<br>
 able to simulate mouse input.<br>
 아두이노 레오나르도 등 프로그램 가능하고 마우스 입력을 수행할 수 있는 보드를 준비합니다.
 7. Upload [the code](Arduino/ultrasonic_mirror.ino) on the Arduino Leonardo.<br>
 아두이노 레오나르도에 [코드](Arduino/ultrasonic_mirror.ino)를 업로드합니다.
 8. Connect Arduino Leonardo with ODROID.<br>
 아두이노 레오나르도와 ODROID를 연결합니다.
 9. Move cursor to upper left of the screen.<br>
 마우스 커서를 화면 왼쪽 위로 옮깁니다.
 10. Done!<br>
 이제 사용할 수 있습니다.

 See [here](Arduino/README.md) to read more details of Arduino setting.<br>
 아두이노 구성에 대한 자세한 정보는 [여기](Arduino/README.md)를 참조하십시오.

## Application file

 you can download pre-built app file from [here](app/release/app-release.apk).

 [여기](app/release/app-release.apk)에서 미리 빌드된 어플리케이션 파일을 다운로드할 수 있습니다.
