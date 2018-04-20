/********************************************************************************
 *  SmartBathroom의 Entry Point Class
 *  - Sensor Control               => sensorControlService package
 *  - BT Serial Communication      
 *  - WiFi Communication           => wiFiSerive package
 *  - STT & TTS										 => speechService package
 * 	- Display UI
 * ********************************************************************************/

import java.io.IOException; 
import java.util.concurrent.TimeoutException;

import livingInformation.MainUI;
import sensorControlService.ControlSensor;
import speechService.simpleSpeech;
import wifiService.WiFiTCPServer;
import btService.BTService;

import commData.CommData;



public class SmartBathroom {
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String TAG 						 = "SmartBathroom";
	
	private ControlSensor					 controlSensor;
  private BTService							 btService;
	private MainUI  							 displayUI;
  private WiFiTCPServer				   wifiServer;
	private simpleSpeech					 simpleSpeech;
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public SmartBathroom() throws IOException {
		super();
		this.controlSensor  = new ControlSensor();											//- initialize Sensor
		this.btService      = new BTService();													//- initialize BTSerial
		//this.displayUI		  = new MainUI();													  //- initialize UI
		
		//this.wifiServer			= new WiFiTCPServer();									  //- initialize WiFi
		this.simpleSpeech		= new simpleSpeech();												//- initialize STT & TTS
	  
		//this.btService.serialBT.open(btService.config);
		
		CommData.log(TAG, "init OK! and Strat !!!");
	}
	

	//-  지정된 거리에 사람 인접 시 음성인식, 양치, 인터폰 기능 동작
	public void runAction(){
		
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Entry Point Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args){
			
			try {
							SmartBathroom smartBath  = new SmartBathroom();
	
			
							while(true)
							{
									switch(CommData.gSTATE)
									{
										  case  CommData.STATE_INIT:  
											      break;
											      
											case  CommData.STATE_LOAD_OK:
											     	smartBath.displayUI		  = new MainUI();													  //- initialize UI
											     	CommData.gSTATE = CommData.STATE_NONE;
													  break;

									    
											case 	CommData.STATE_NONE:
												    CommData.log(TAG, " --- STATE_NONE");
												        //- 초음파 센서에 따른 STATE_START 상태 변화
														try {
															    CommData.gDistance = smartBath.controlSensor.ultrasonic.measureDistance();
															    CommData.log(TAG, "CommData.gDistance - "+CommData.gDistance);
															    
																	if(CommData.gDistance <= CommData.LIMIT_DIS)
																			CommData.gSTATE = CommData.STATE_START_INIT;
																	
																	Thread.sleep(1000);			
																	CommData.log(TAG, "Action Start ~!");
															
														} catch (TimeoutException e) {
																	e.printStackTrace();  CommData.log(TAG, " --- STATE_NONE : TimeoutEX " + e.getMessage());
														} catch (InterruptedException e) {
																	e.printStackTrace();  CommData.log(TAG, " --- STATE_NONE : InterruptEX " + e.getMessage());
														}
														
														break;
														

														
											case 	CommData.STATE_START_INIT:
												    CommData.log(TAG, " --- STATE_START_INIT");
												    smartBath.btService.serialBT.open(smartBath.btService.config);   //- start serialBT 
												    smartBath.controlSensor.startThread();
												    smartBath.displayUI.displayInfomation();
												    //smartBath.wifiServer.start();
												    smartBath.simpleSpeech.startThread();
												    CommData.gSTATE = CommData.STATE_START;
												    break;
												    
												       
											case 	CommData.STATE_START:
												    //CommData.log(TAG, " --- STATE_START");
												        //- 사람 인지에 따른 생활정보 출력, 음성인식, 양치, 인터폰 기능 구동
												    
												    break;
												    
											case 	CommData.STATE_STOP:
											      //CommData.log(TAG, " --- STATE_STOP");
												    smartBath.controlSensor.stopThread();
												    smartBath.simpleSpeech.stopThread();
												    smartBath.displayUI.hiddenInfomation();
												    CommData.gSTATE = CommData.STATE_NONE;
											      break;		  
									}
							}
			
			} catch (IOException ioe) {
					ioe.printStackTrace();
					CommData.log(TAG, ioe.getMessage());
			} 
	}  // main()

}
