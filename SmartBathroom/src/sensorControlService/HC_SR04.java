package sensorControlService;

import java.util.concurrent.TimeoutException;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import commData.CommData;


public class HC_SR04 {

	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String 				TAG 											= "HC_SR04";
	
  private final static float 					SOUND_SPEED 							= 340.29f;  // speed of sound in m/s
    
  private final static int 						TRIG_DURATION_IN_MICROS 	= 10	; 			// trigger duration of 10 micros
  //private final static int 						WAIT_DURATION_IN_MILLIS 	= 60; 			  // wait 60 millis
  private final static int 						TIMEOUT 									= 2100;
	
  
	private final GpioPinDigitalInput    echoPin;
	private final GpioPinDigitalOutput		trigPin;
	

	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public HC_SR04(Pin echoPin, Pin trigPin) {
		super();
		
		this.echoPin = CommData.GPIO.provisionDigitalInputPin( echoPin );
		this.trigPin = CommData.GPIO.provisionDigitalOutputPin( trigPin );
		this.trigPin.low();
		
		CommData.log(TAG, "ultrasonic init OK");
	}
	
	
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- 초음파 발사 신호 출력 
  private void triggerSensor() {
        try {
		            this.trigPin.high();
		            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
		            this.trigPin.low();
        } catch (InterruptedException ex) {
            		System.err.println( "Interrupt during trigger" );
        		}
    }
	   

    //- 반사되어 돌아오는 초음파 신호 대기 
  private void waitForSignal() throws TimeoutException {
      int countdown = TIMEOUT;
      
      while( this.echoPin.isLow() && countdown > 0 ) {
          countdown--;
      		}
      
      if( countdown <= 0 ) {
         // throw new TimeoutException( "Timeout waiting for signal start" );
      		}
   }  
  
    //- 반사 초음파 시간 계산 
  private long measureSignal() throws TimeoutException {
	  
      int countdown = TIMEOUT;
      long start = System.nanoTime();
      
      while( this.echoPin.isHigh() && countdown > 0 ) {
          countdown--;
     		}
      
      long end = System.nanoTime();
      
      if( countdown <= 0 ) {
          //throw new TimeoutException( "Timeout waiting for signal end" );
      		}
      
      return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
  	}
  
  
    //- 반사 시간을 이용한 cm 거리 계산 
  public int measureDistance() throws TimeoutException {
      this.triggerSensor();
      this.waitForSignal();
      long duration = this.measureSignal();
      
      return (int)(duration * SOUND_SPEED / ( 2 * 10000 ));
    }
  
}
