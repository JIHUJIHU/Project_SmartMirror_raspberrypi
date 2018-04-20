package sensorControlService;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import commData.CommData;

public class DCMotor {
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String 			  TAG 		 = "DCMotor";
	
  private final GpioPinDigitalOutput   motor01_Pin;
  private final GpioPinDigitalOutput   motor02_Pin;
  private final GpioPinDigitalOutput   motor03_Pin;
  private final GpioPinDigitalOutput   motor04_Pin;
  
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public DCMotor(Pin motor01_Pin, Pin motor02_Pin, Pin motor03_Pin, Pin motor04_Pin) {
		super();
	
		this.motor01_Pin = CommData.GPIO.provisionDigitalOutputPin( motor01_Pin);
		this.motor02_Pin = CommData.GPIO.provisionDigitalOutputPin( motor02_Pin);
		this.motor03_Pin = CommData.GPIO.provisionDigitalOutputPin( motor03_Pin);
		this.motor04_Pin = CommData.GPIO.provisionDigitalOutputPin( motor04_Pin);
		this.motor01_Pin.low();
		this.motor02_Pin.low();
		this.motor03_Pin.low();
		this.motor04_Pin.low();
		
		CommData.log(TAG, "init OK");
	}

	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void onDCMotor(){
		this.motor01_Pin.high();
		this.motor02_Pin.high();
		this.motor03_Pin.high();
		this.motor04_Pin.high();
	}
	
	public void offDCMotor(){
		this.motor01_Pin.low();
		this.motor02_Pin.low();
		this.motor03_Pin.low();
		this.motor04_Pin.low();
	}
	
}
