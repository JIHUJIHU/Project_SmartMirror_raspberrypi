package livingInformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import commData.CommData;

public class WeatherData {
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String				 TAG = "WeatherData";
	
	public static LinkedList<WeatherSB> weatherList;
	
	private final String 							 urlstr = "http://api.wunderground.com/api/b3824d433c6dba75/conditions/forecast10day/lang:KR/q/Korea/DAEGU.json";//OpenAPI call하는 URL
	
	public JSONObject 									weatherJson;
	public JSONParser 								  jsonParser;
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void getJson(){
		try{
						URL url = new URL(urlstr);
            BufferedReader bf;
            String line;
            String result="";

            bf = new BufferedReader(new InputStreamReader(url.openStream()));

            			//버퍼에 있는 정보를 문자열로 변환.
            while((line=bf.readLine())!=null){
                result=result.concat(line);
            			}
            bf.close();

            			//문자열을 JSON으로 파싱
            jsonParser = new JSONParser();
            weatherJson = (JSONObject)jsonParser.parse(result);

		}catch (Exception e) {
				CommData.log(TAG,"날씨 정보를 가져오는데 실패하였습니다.");
		}
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-  현재 날씨 정보 가져오는 메소드
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public WeatherSB getCurrentWeather(){ //현재 날씨 가져오기
				getJson();
        JSONObject current_observation = (JSONObject) weatherJson.get("current_observation"); //현재 날씨 정보
        String temp_C = current_observation.get("temp_c")+"";  //현재 기온 섭씨 출력
        String status = current_observation.get("icon")+"";
        
				return new WeatherSB(temp_C, status);
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-  내일~3일후 날씨  정보 가져오는 메소드
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public LinkedList<WeatherSB> getForecastList(){ 
		
		weatherList = new LinkedList<WeatherSB>();
		
		try {
						getJson();
    				JSONObject tenDay = (JSONObject)weatherJson.get("forecast");
            JSONObject txt_forecast = (JSONObject) tenDay.get("txt_forecast");
            JSONArray forecast10day = (JSONArray) txt_forecast.get("forecastday");
            
            for(int i=0; i<3; i++){
	            	JSONObject weather = (JSONObject) forecast10day.get(i*2+2); //1일 단위로 가져옴 (홀수는 저녁)
	            	
	            	String title = weather.get("title").toString().replace("요일", "");
	            	String status = weather.get("icon").toString();
	            	String fcttext_metric = weather.get("fcttext_metric").toString();
	            	
	            	weatherList.add(new WeatherSB(title, status, fcttext_metric));
            			}
		}catch (Exception e) {
			 	CommData.log(TAG,"날씨 정보를 가져오는데 실패하였습니다.");
		}
		return weatherList;
	}
}
