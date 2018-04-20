package sensorControlService;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import commData.CommData;
 


public class DHT11{
	
       //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
   //- Member Variable
       //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
	  private final static String 				TAG 					= "DHT11";

	  private final int       							dhtPin;
	  
    private static final int 						MAXTIMINGS 		= 85;
    private int[]   											dht11_dat 		= { 0, 0, 0, 0, 0 };
    private float	 											hum;
    private float  											tempC, tempF;
    
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    //- Member Method : Constructor
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    public DHT11(int dhtPin) {
    	
    		super();
    		//this.gpio = gpio;
    		this.dhtPin = dhtPin;

        GpioUtil.export(this.dhtPin, GpioUtil.DIRECTION_OUT);     
        
        CommData.log(TAG, "init OK");
       }
 
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    //- Member Method
        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
       //- 온습도 구하기 
    public void getHumTemperature()
        {
       int laststate = Gpio.HIGH;
       int j = 0;
       dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
 
       Gpio.pinMode(this.dhtPin, Gpio.OUTPUT);
       Gpio.digitalWrite(this.dhtPin, Gpio.LOW);
       Gpio.delay(18);
 
       Gpio.digitalWrite(this.dhtPin, Gpio.HIGH);        
       Gpio.pinMode(this.dhtPin, Gpio.INPUT);
 
       for (int i = 0; i < MAXTIMINGS; i++)
              {
          int counter = 0;
          
          while (Gpio.digitalRead(this.dhtPin) == laststate) {
              counter++;
              Gpio.delayMicroseconds(1);
              if (counter == 255) break;
          			}
 
          laststate = Gpio.digitalRead(this.dhtPin);
 
          if (counter == 255) break; 
 
          /* ignore first 3 transitions */
          if ((i >= 4) && (i % 2 == 0)) {
             dht11_dat[j / 8] <<= 1;
             if (counter > 16) { dht11_dat[j / 8] |= 1; }
             j++;
           			}
       			}
       
        if ((j >= 40) && checkParity())
                {
            float h = (float)((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if ( h > 100 ) h = dht11_dat[0];   // for DHT11
            			
            float c = (float)(((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if ( c > 125 ) c  = dht11_dat[2];   // for DHT11
            			
            if ( (dht11_dat[2] & 0x80) != 0 ) c = -c;
            		    
            float f = c * 1.8f + 32;
            //System.out.println( "Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
            
            if(h>1) this.hum = h;
            if(c>1) this.tempC = c;
            if(f>1) this.tempF = f;
            		   
            //System.out.println( "this.hum = " +this.hum + " this.tempC = " + this.tempC + "  this.tempF" + this.tempF + "f)");
        }else{
           // System.out.println( "Data not good, skip" );
        		}
 
       }
 
   public float getHum() {
		return hum;
	  }

	 public float getTempC() {
		return tempC;
	 }

	 public float getTempF() {
		return tempF;
	  }

	  //- 패리티 비트 검사 -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   private boolean checkParity() {
      return (dht11_dat[4] == ((dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3]) & 0xFF));
      }
 
 }