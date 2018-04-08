
// ----------------------------------------------------------------------------
// 버튼 클릭 시 멜로디 출력  + 사진 찍기
// 찍은 사진 WiFi로 전송
// 서버로부터 받은 결과 Open & Close에 따라서 모터 제어 하기
// ----------------------------------------------------------------------------

#include <SPI.h>
#include <WiFly.h>
#include <Adafruit_VC0706.h>
#include <pitches.h>
#include <PlayMelody.h>
#include <Servo.h>
#include <String.h>



#define melodyPin 3
 

//- 센서 연결 핀 선언 ---------------------------------------------------------- 
#define PUSH_PIN        48

//- 전역변수 ------------------------------------------------------------------- 
char    passphrase[]       = "";
char    ssid[]             = "iptime";
char    serverIP[]         = "192.168.1.22";
boolean bConnectAP        = false;
boolean bConnectServer    = false;
String proto = "";

Servo myservo;

int pos=0;

 

//- 객체 변수 선언 -------------------------------------------------------------
WiFlyClient             client(serverIP, 3000);
Adafruit_VC0706         cam = Adafruit_VC0706(&Serial1);


//- 초기화 ---------------------------------------------------------------------
void setup() {
  Serial.begin(9600);
  cam.begin(38400);
  initWifi();
  initCamera();
  pinMode(PUSH_PIN,INPUT_PULLUP);
  pinMode(melodyPin, OUTPUT);
  myservo.attach(4);
}


//- 기능 구현 ------------------------------------------------------------------
void loop() {
  int a = digitalRead(PUSH_PIN);
 if(bConnectAP)
  {
          if(client.connected()) 
          {
              //- Server로부터 수신한 데이터 처리
              if (client.available()){
                      char c = client.read();
                      Serial.print(c);
               }
              if(a==LOW){
                Serial.println("Play_Sing");
                  delay(500);
                  shutCamera(&client);
                  PlayMelody(melodyPin, "t380r8v12l16a>dv13fav14>dfv15afv14d<av13f8r4v12<a>cv13eav14>cev15aev14c<av13er4<c-c<a+b>d+f+abb+a+bn66a+bn66r2erfrerd+r2rdrerdrc+r>>crdrcr<brarb+rbrarg+rfrerd+rerdrcl64ab>cdefgab>cdev11l16<<ag+abb+b>cdedcc-dc<bg+ag+abb+b>cdedcc-dc<bg+ag+a>ceceafdn70dfn70dfe<ab+ab+eaeg+8ef+gef+g+ag+abb+b>cdedcc-dc<bg+ag+abb+b>cdedcc-dc<bg+ag+a>ceceafdn70dfn70dfe<ab+ab+eaeg+8ef+gef+g+v13ag+abb+b>cdfn70dfed+e8ec<a>cc<aea>ec<a>caeceaecebg+eg+aecebg+eg+aecebg+eg+aecebg+eg+aeg+eaeg+eaeg+eaeg+eaeg+eaeg+eaeg+eaeg+ea8r4.g+8r4.v15ar2.r8");                            
                  PlayMelody(melodyPin, "O4"); // 옥타브를 다시 초기값 4로 설정
                  while(1){
                  String door = client.readString();
                  
                  if(door.equals("@DOOR,OPEN#")){
                     Serial.println("open"); 
                     
                      PlayMelody(melodyPin, "ea8r4.g+8r4.v15ar2.r8");                            
                      PlayMelody(melodyPin, "O4");
                     doorOpen(); 
                     delay(5000);
                     doorClose();
                     break;
                  }
                  else if(door.equals("@DOOR,CLOSE#")){
                     Serial.println("close");
                     
                      
                      PlayMelody(melodyPin, "r8.v15ar2.ea8r4.g+8r4");                            
                      PlayMelody(melodyPin, "O4");
                      doorClose();
                     break;
                  }
                  }
                }
              //- Server로 데이터 보내기  
          }else{
             bConnectServer = client.connect(); 
             Serial.print("Server Connect : ");
             if(bConnectServer==1){
             Serial.println("connect");
             }
             else{
               Serial.println("Not connect");
             }
          }
          
   }else{ Serial.println("Disable WiFi"); }
  
}

void doorOpen(){
   myservo.write(180); 
}

void doorClose(){
   myservo.write(90);
}
//- WiFi 켜기 ------------------------------------------------------------------
void initWifi()
{
    Serial.println("Init Wifly..");
    WiFly.begin();

    Serial.print("Wifly joining...");
    if (!WiFly.join(ssid, passphrase,true)) 
    {
          Serial.println("Disable Connect AP");
          Serial.println("Connect Fail");
          return;
    }
    
    Serial.println("Join OK");
    Serial.print("Clinet IP : ");
    String strIP  =  "";
    do{
        strIP = WiFly.ip();
    }while(strIP.equals("0.0.0.0"));
   
    Serial.println(strIP);
    bConnectAP = true;
    Serial.println("Wifi Client Init OK");
}


//- WiFi통신으로 수신된 데이터 처리 ------------------------------
void phaseCommand(String strRxData){

  String strCmd = strRxData.substring(1,4);
  String strData = strRxData.substring(5);
  
}

//- 카메라 초기화 ------------------------------------------------------------
void initCamera() {    
    if(cam.begin(38400)) {                 //- 카메라 연결 여부 체크         
        Serial.println("Camera fount.");    
    }else{        
        Serial.println("No Camera.");        
        return ;    
    }        
    
    char *rep = cam.getVersion();          //- 카메라 버전 체크    
    if(rep == 0){         
        Serial.println("Failed to get version.");    
    }else {        
        Serial.print("version : ");        
        Serial.println(rep);    
    }        
    
    cam.setImageSize(VC0706_320x240);    //- 촬영 이미지 사이즈 지정    
    byte imgsize = cam.getImageSize();    
    Serial.println("Image Size: 320x240"); 
}

//- 카메라 사진 찍기---------------------------------------------------------
void shutCamera(WiFlyClient* client) {  
   
   Serial.println("Shut Camera.");  
  
   if(cam.takePicture()) {                    //- 사진 촬영    
      Serial.println("Picture taken!");  
   }else{
     Serial.println("Picture taken!"); 
     return ;  
   }    
  
   int jpglen = cam.frameLength();          //- 촬영된 사진 사이즈  
   Serial.print("Image length:");  
   Serial.println(jpglen);
   int jpgleng = jpglen;
   String leng = String(jpglen);
   add("@IMG,",leng);
   client->print(proto);
//   client->print("@IMG,"+String(jpglen));                 //- WiFi로 촬영된 사진 전송  
  // Serial.print("@IMG,");
 
 //  if(String(jpglen).equals(String(jpglen)))
  // Serial.print(String(jpglen));
   Serial.print(",");

   while(jpgleng > 0) 
   {      
      byte *buffer;      
      byte byteToRead = min(32, jpgleng);      
      buffer = cam.readPicture(byteToRead);      
      client->write(buffer, byteToRead);      
      jpgleng -= byteToRead;  
    }  
    client->print("####");  
    Serial.print("####");
    cam.resumeVideo(); 
}

void add(String s, String t){
   proto = s + t; 
}

