package livingInformation;

import java.awt.Image;

import javax.swing.ImageIcon;

public class WeatherSB {
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public String 			title; 								//요일
	public String 			status; 							//날씨상태
	public String 			fcttext_metric; 			//예보
	public String 			temp_C; 							//기온 (현재날씨인 경우에만 있음, 나머지 null)
	public ImageIcon 		iconImg;

	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public WeatherSB(String title, String status, String fcttext_metric){
		this.title = title;
		this.status = status;
		this.fcttext_metric = fcttext_metric;
		setIconImg(status);
	}
	
	
	public WeatherSB(String temp_C, String status){
		this.temp_C = temp_C;
		this.status = status;
		this.title = null;
		this.fcttext_metric = null;
		setIconImg(status);
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void setIconImg(String status){
		try{
		  ImageIcon image = new ImageIcon(WeatherSB.class.getResource("Weather/icon/"+status+".png"));
    	Image img = image.getImage();
    	this.iconImg = new ImageIcon(img.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
		}catch(Exception e){
			System.out.println("아이콘 로드 오류");
		}
	}
}
