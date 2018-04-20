package commData;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class CommData {
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable 
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- BT & WiFi 통신 프로토콜 
	public static final String  TOOTH_BEGIN = "@TOOTH,1#";
	public static final String  TOOTH_END   = "@TOOTH,0#";	
	
	public static final String  DOOR_OPEN   = "@DOOR,OPEN#";
	public static final String  DOOR_CLOSE  = "@DOOR,CLOSE#";
	
	
	public static final String  OPEN  			= "open";
	public static final String  CLOSE  			= "close";
	
	//- 프로그램 동작 상태 상수
	public static final int			STATE_NONE  				 = 0;
	public static final int			STATE_START_INIT 		 = 1;							//- 지정된 거리에 사람 인접
	public static final int			STATE_START 				 = 2;							//- 지정된 거리에 사람 인접
	public static final int			STATE_STOP  				 = 3;
	public static final int			STATE_LOAD_OK 			 = 4;
	public static final int			STATE_INIT		 			 = 5;

	public final static int			LIMIT_HUM   = 50;									//- 습도 센서 임계치 %
	public final static int			LIMIT_DIS   = 60;									//- 초음파 센서 임계치 cm
	
	public static String					gVOICE_RESULT = "";								//- 음성 인식 결과 저장 변수
	public static String					gRX_DATA 		  = "";								//- 시리얼 양치 정보 저장 변수
	public static int						gSTATE 				= STATE_INIT;				//- 프로그램 동작 상태 저장 변수 
	
	public static int						gDistance			= 0;
	public static float				  gHum					= 0.0f;
	public static float					gTemp					= 0.0f;
	
	public static boolean				gBRUSH				= false;	
	
	public final static GpioController GPIO   = GpioFactory.getInstance();

	
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method 
	//- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void log(String szTitle, String szMsg){
			System.out.println("["+szTitle+"] "+szMsg);
	}

}
