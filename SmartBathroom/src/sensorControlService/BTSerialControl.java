package sensorControlService;

import java.io.IOException;
import java.util.Date;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;


public class BTSerialControl {

	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static String RX_DATA = "";
	  
	  
		
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	// --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
  public BTSerialControl() {
		super();
		// TODO Auto-generated constructor stub
	}



	public static void main(String args[]) throws InterruptedException, IOException {


        final Console console = new Console();
        console.title("<-- The Pi4J Project -->", "Serial Communication Example");
        console.promptForExit();

                //- 시리얼 통신 객체 생성
        final Serial serial = SerialFactory.createInstance();				//- 시리얼 통신 객체 생성
        
               //- 시리얼 통신 리스너 생성 및 등록
        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // print out the data received to the console
                try{
                    	//console.println("[HEX DATA]   " + event.getHexByteString());
                    	//console.println("[ASCII DATA] " + event.getAsciiString());
                    	RX_DATA = event.getAsciiString();
                    	console.println("RX_DATA = " + RX_DATA );
                    	
                }catch (IOException e){ e.printStackTrace(); }
            			}
        		});

        try {
            			//- 시리얼 통신 설정 객체 생성
            SerialConfig config = new SerialConfig();
            config.device("/dev/ttyAMA0")
                  .baud(Baud._115200)
                  .dataBits(DataBits._8)
                  .parity(Parity.NONE)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);

                        //- 기본 세팅값 체
            if(args.length > 0){
                config = CommandArgumentParser.getSerialConfig(config, args);
            			}

            // display connection details
                        /*
            console.box(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below."); */

                        //- 시리얼 통신 포트 및 디바이스 Open
            serial.open(config);

            // continuous loop to keep the program running until the user terminates the program
            while(console.isRunning())
                        {
                try{
		                    // write a formatted string to the serial transmit buffer
		                    serial.write("CURRENT TIME: " + new Date().toString());
		
		                    // write a individual bytes to the serial transmit buffer
		                    serial.write((byte) 13);
		                    serial.write((byte) 10);
		
		                    // write a simple string to the serial transmit buffer
		                    serial.write("Second Line");
		
		                    // write a individual characters to the serial transmit buffer
		                    serial.write('\r');
		                    serial.write('\n');
		
		                    // write a string terminating with CR+LF to the serial transmit buffer
		                    serial.writeln("Third Line");
                }catch(IllegalStateException ex){
                    ex.printStackTrace();
                				}

                // wait 1 second before continuing
                Thread.sleep(1000);
                        	}

        	 }catch(IOException ex) {
        			console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
        			return;
        			  }
   			}
}
