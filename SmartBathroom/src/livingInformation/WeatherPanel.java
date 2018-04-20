package livingInformation;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//생활정보 - 날씨(패널 상단의 오른쪽)
public class WeatherPanel extends JPanel{
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID 		= 1L;
	//http://api.wunderground.com/api/b3824d433c6dba75/conditions/forecast10day/lang:KR/q/Korea/DAEGU.json
	//private final String urlstr = "http://api.wunderground.com/api/b3824d433c6dba75/conditions/" 
 //  														+ "forecast10day/lang:KR/q/Korea/DAEGU.json";  //OpenAPI call하는 URL
	
	private GetCurrentWeather getCurrentWeather;
	private GetForecast 			 getForecast;
	
	private JLabel 						 currentDegree; 		//현재 기온
	private JLabel 						 currentIcon;
	private JPanel 						 southPanel; 			//forecastPanel이 들어가기 위한 패널
	private JPanel 						 forecastPanel[] = new JPanel[3]; //내일~ 3일후 까지의 예보가 들어가기 위한 패널
	private JLabel 						 forecastIcon[]  = new JLabel[3];
	private JLabel 						 forecastTitle[] = new JLabel[3];
    
	private WeatherData 			 weatherData = new WeatherData();
    
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public WeatherPanel(){
		
		System.out.println(weatherData.getForecastList().size());
    	
    setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(50 , 0 , 0 , 50));
		setBackground(Color.BLACK);
		
		currentDegree = new JLabel("null℃", SwingConstants.RIGHT);
		currentDegree.setVerticalAlignment(SwingConstants.TOP);
		currentDegree.setFont(new Font("함초롬돋움", Font.PLAIN, 70));
		currentDegree.setForeground(Color.WHITE);
		
		currentIcon = new JLabel();
		currentIcon.setVerticalAlignment(SwingConstants.TOP);
		currentIcon.setHorizontalAlignment(SwingConstants.RIGHT);

		southPanel = new JPanel(); //날씨 패널 하단
		southPanel.setLayout(new FlowLayout());
		southPanel.setBackground(Color.BLACK);
		southPanel.setBorder(BorderFactory.createEmptyBorder(30 , 0 , 0 , 0));
		
		for(int i=0; i<forecastPanel.length; i++){
			forecastIcon[i] = new JLabel();
			
			forecastTitle[i] = new JLabel();
			forecastTitle[i].setHorizontalAlignment(SwingConstants.CENTER);
			forecastTitle[i].setForeground(Color.WHITE);
			forecastTitle[i].setFont(new Font("함초롬돋움", Font.BOLD, 18));
		}
		
		for(int i=0; i<forecastPanel.length; i++){
			  forecastPanel[i] = new JPanel(); //날씨 패널 하단
    		forecastPanel[i].setLayout(new BorderLayout());
    		forecastPanel[i].setBackground(Color.BLACK);
    		forecastPanel[i].add(forecastTitle[i], BorderLayout.NORTH);
    		forecastPanel[i].add(forecastIcon[i], BorderLayout.SOUTH);
    		southPanel.add(forecastPanel[i]);
		}
		
		this.add(currentDegree, BorderLayout.CENTER);
		this.add(currentIcon, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
		
		this.getCurrentWeather = new GetCurrentWeather();
		this.getCurrentWeather.start();

		this.getForecast = new GetForecast();
		this.getForecast.start();
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Inner Class  -  GetCurrentWeather
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	class GetCurrentWeather extends Thread{
		@Override
		public void run() {
			while(true){
				try {
			    	WeatherSB current = weatherData.getCurrentWeather();
			    	
			    	System.out.println("현재 기온 : " + current.temp_C + "\n" + "현재 상태 : " +current.status);
			    	
			    	currentDegree.setText(current.temp_C + "℃");
			    	currentIcon.setIcon(current.iconImg);
			    	
					sleep(3000000); //1분마다 작동
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	class GetForecast extends Thread{
		
		@Override
		public void run() {
				while(true)
				{
					try {
				    	List<WeatherSB> forecastList = weatherData.getForecastList();
				    	
				    	for(int i=0; i<3; i++){
				    		WeatherSB curWeather = forecastList.get(i);
				    		forecastTitle[i].setText(curWeather.title);
				    		forecastIcon[i].setIcon(curWeather.iconImg);
				    	}
						sleep(3600000); //1시간마다 작동'
						
					} catch (Exception e) {
							e.printStackTrace();
					}
				} // while()
				
		} //- run()
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
