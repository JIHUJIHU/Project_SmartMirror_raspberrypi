package sensorControlService;


import java.util.List;

import livingInformation.WeatherSB;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import commData.CommData;




public class ControlSensor {
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable 
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String TAG 						 	 	  = "ControlSensor";

	private final static Pin     				DLUX_PIN  	  = RaspiPin.GPIO_02;
	private final static Pin     				ECHO_PIN  	  = RaspiPin.GPIO_07;
	private final static Pin     				TRIG_PIN  	  = RaspiPin.GPIO_00;
	private final static Pin     				LED_PIN   	  = RaspiPin.GPIO_21;
	
	private final static Pin     				MOT_01_Pin    = RaspiPin.GPIO_22;
	private final static Pin     				MOT_02_Pin    = RaspiPin.GPIO_23;
	private final static Pin     				MOT_03_Pin    = RaspiPin.GPIO_24;
	private final static Pin     				MOT_04_Pin    = RaspiPin.GPIO_25;
	
	private final static int     				DHT11_PIN 	  = 3;
	
	
	public  LED														blinkLED;
	public  HC_SR04												ultrasonic;
	public  DigitalCDS									  cds;
	public  DHT11												  dht11;
	public  DCMotor												dcMotor;
	private CheckSensor										checkThread  = null;
	
	//private static simpleSpeech 					speech;
	//private final Serial 								serial 			  = SerialFactory.createInstance();	 //- 시리얼 통신 객체 생성
	//private static String								rx_tooth_data = "";
	//public static String voiceResult = "";
	/*
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method 
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void log(String szTitle, String szMsg){
			System.out.println("["+szTitle+"] "+szMsg);
	}
	*/
	
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method 
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControlSensor(){
		//- 센서 초기화  
		if(Gpio.wiringPiSetup() == -1) { CommData.log(TAG, "GPIO SETUP FAILED");  return; }
		
		this.blinkLED     = new LED(LED_PIN);
		this.ultrasonic   = new HC_SR04(ECHO_PIN, TRIG_PIN);
		this.cds   			   = new DigitalCDS(DLUX_PIN);
		this.dht11		 		 = new DHT11(DHT11_PIN);
		this.dcMotor  		 = new DCMotor(MOT_01_Pin, MOT_02_Pin, MOT_03_Pin, MOT_04_Pin);
		//this.checkThread  = new CheckSensor();
		
		
		CommData.log(TAG, "init OK");
		
		
/*
      
            //- 시리얼 통신 포트 및 디바이스 Open
      try {
						serial.open(config);
			} catch (IOException e) { e.printStackTrace(); }*/
	}
	
  public void startThread(){
		 	if(this.checkThread == null){
		 		    this.checkThread  = new CheckSensor();
		 				CommData.log(TAG+" - CheckSensor THREAD : ", " running... ");
		 				this.checkThread.start();
		    }
    }
  
  public void stopThread(){
	  
	  if(this.checkThread != null){
		  this.checkThread.interrupt();
		  this.checkThread = null;
		  CommData.log(TAG+" - CheckSensor THREAD : ", " STOP ");
	  }
   }
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Inner Class  CheckSensor
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	class CheckSensor extends Thread{


		@Override
		public void run() {
			   
				while(true)
				{
						try{
									CommData.gDistance =  ultrasonic.measureDistance();
								  CommData.log(TAG, "CommData.gDistance - "+CommData.gDistance);
								    
									if(CommData.gDistance > CommData.LIMIT_DIS+10)
												CommData.gSTATE = CommData.STATE_STOP;
									
									dht11.getHumTemperature();
									CommData.gHum = dht11.getHum();
									if(CommData.gHum >= CommData.LIMIT_HUM) dcMotor.onDCMotor(); else dcMotor.offDCMotor();
									
									
									blinkLED.controlLED(cds.getLight());
									
									//CommData.log(TAG+" - CheckSensor THREAD : ", " CommData.gDistance : " + CommData.gDistance + ",  CommData.gHum : " + CommData.gHum);
									
								  sleep(2600); //2.6초마다 체크
							
						} catch (Exception e) {
								CommData.log(TAG, e.getMessage());
						}
				} // while()
				
		} //- run()
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/*
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Entry Point Method
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {

		mainUI m = new mainUI();
		speech 										 = new simpleSpeech(); //음성인식 및 관련 스레드 실행
		

		
		ControlSensor     controlSensor = new ControlSensor();
		
		int								distance = 0;
		float							hum =0;
		
		
		if(Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
		}

		while(true)
		{
				
				try{
						  Thread.sleep(700);
						   
						    //- 초음파 센서 활용한 거리 측정 후 일정거리 접근 시 생활 정보 화면 출력
						  distance = hc_sr04.measureDistance();
							//log("Distance", distance+"cm");
						  if(distance<=10){
							  m.setVisible(true);
						  }
							
							//- 조도 센서를 활용한 LED 제어
							redLED.controlLED(myDCDS.getLight());
							
							//- 온습도 센서를 활용한 DC모터 제어
							dht.getTemperature();
							hum = dht.getHum();
							//log("hum", hum+"%");
							if(hum >= LIMIT_HUM) windDCM.onDCMotor(); else windDCM.offDCMotor();
						
				} catch (TimeoutException te) {
					//	log( TAG+"- ERROR", te.getMessage());
				} catch (InterruptedException ie) {
					  log( TAG+"- ERROR", ie.getMessage());
				}
		}
	}
  */
}
