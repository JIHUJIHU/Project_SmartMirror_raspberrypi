package livingInformation;

import java.awt.BorderLayout;   
import java.awt.Color;

import javax.swing.JPanel;


public class LivingInformationNorth extends JPanel{
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID 		= 1L;
	

	public LivingInformationNorth(){
	  this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.BLACK);
		this.add(new NowDateTimePanel(), BorderLayout.WEST);	// 패널 좌측에 시간패널 추가
		this.add(new WeatherPanel(), BorderLayout.EAST); 		  // 패널 우측에 날씨 패널 추가 
	}
	
}
