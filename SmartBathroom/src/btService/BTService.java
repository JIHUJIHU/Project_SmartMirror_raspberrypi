package btService;

import java.io.IOException;

import brush.Progress;

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
import commData.CommData;


public class BTService {
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final String 						 TAG 					= "BTService";
	
	public  Serial 										 serialBT; 	
	public  SerialConfig 							 config;
	private SerialDataEventListener   dataListener;
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public BTService() {
		super();
		
		this.serialBT = SerialFactory.createInstance();	 //- 시리얼 통신 객체 생성
		this.config   = new SerialConfig();
	  this.config.device("/dev/ttyAMA0")
         .baud(Baud._115200)
         .dataBits(DataBits._8)
         .parity(Parity.NONE)
         .stopBits(StopBits._1)
         .flowControl(FlowControl.NONE); 
		
		this.dataListener = new SerialDataEventListener(){

			@Override
			public void dataReceived(SerialDataEvent event) {
				System.out.println("bluetooth Successful");
				try {
								CommData.gRX_DATA = event.getAsciiString();
								if(CommData.gRX_DATA.equals(CommData.TOOTH_BEGIN)){
										//- 양치 애니메이션 시작
										CommData.gBRUSH = true;
										Progress sp = new Progress();
							    	sp.start();
										CommData.log(TAG, CommData.TOOTH_BEGIN);
								}else if(CommData.gRX_DATA.equals(CommData.TOOTH_END)){
									if(CommData.gBRUSH == true)
									{
											//- 양치 애니메이션 종료
											CommData.log(TAG, CommData.TOOTH_END);
											Progress sp = new Progress();
								    	sp.toggleProgress(false);
								    	Progress.Pcount = 100;
								    	CommData.gBRUSH = false;
									}
								}					
				} catch (IOException e) { e.printStackTrace(); }
				
			}};
			
		this.serialBT.addListener(this.dataListener);
		
		CommData.log(TAG, "init OK");
		
		/*
		//- 시리얼  송수신 데이터 리스너 생성 및 등록
		this.serialBT.addListener(new SerialDataEventListener(){

			@Override
			public void dataReceived(SerialDataEvent event) {
				
        try{
        				rx_tooth_data = event.getAsciiString();
        				System.out.println("RX_DATA = " + rx_tooth_data );
        				
				  		  if(rx_tooth_data.equals(CommData.TOOTH_BEGIN)){
				  			
				  		  }else if(rx_tooth_data.equals(CommData.TOOTH_BEGIN)){
        				  			
        				  		}
        				      
        	}catch (IOException e){ e.printStackTrace(); }
			}});
	
		    //- 시리얼 통신 설정 객체 생성
      SerialConfig config = new SerialConfig();
      config.device("/dev/ttyAMA0")
            .baud(Baud._115200)
            .dataBits(DataBits._8)
            .parity(Parity.NONE)
            .stopBits(StopBits._1)
            .flowControl(FlowControl.NONE);
            	*/
	}

}
