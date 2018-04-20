package livingInformation;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

//생활정보 - 시간날짜 (패널 상단의 왼쪽)
public class NowDateTimePanel extends JPanel{
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID 		= 1L;
	
	private JLabel   					timeLabel;
	private JLabel   					dateLabel;
	private Timer    					timer;
	
	private String[] 					dayOfWeek  = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private String[] 					monthNames = {"January", "February", "March", "April", "May", "June", "July",
						   					 									"August", "September", "October", "November", "December"};
	private Calendar 					calendar;
	
	private SimpleDateFormat nowDate 		= new SimpleDateFormat("dd", Locale.US);
	private SimpleDateFormat nowTime 		= new SimpleDateFormat("h:mm a", Locale.US);
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public NowDateTimePanel(){
		
		setBorder(BorderFactory.createEmptyBorder(40 , 30 , 0 , 0)); //상하좌우 10씩 띄우기
		
		timeLabel = new JLabel(" 00:00 AM", SwingConstants.LEADING);
		timeLabel.setVerticalAlignment(SwingConstants.TOP);
		timeLabel.setFont(new Font("함초롬돋움", Font.BOLD, 75));
		timeLabel.setForeground(Color.WHITE);
		    
		dateLabel = new JLabel("　nowDate", SwingConstants.LEADING);
		dateLabel.setVerticalAlignment(SwingConstants.TOP);
		dateLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 40));
		dateLabel.setForeground(Color.WHITE);
		setBackground(Color.BLACK);
		setLayout(new BorderLayout(0, 0));
		this.add(timeLabel, BorderLayout.NORTH);
		this.add(dateLabel, BorderLayout.CENTER);
		
		timer = new Timer();
		timer.start();
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Inner Class
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Timer extends Thread{
		Date date;
		
		@Override
		public void run() {
			
			while(true)
			{
					try {
									date = new Date();
									calendar = Calendar.getInstance();
									String day = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1];
									String month = monthNames[calendar.get(Calendar.MONTH)];
									
									timeLabel.setText(nowTime.format(date));//시간표시
									dateLabel.setText(day + ", " + month + " " + nowDate.format(date)); 	//요일, 월, 일 표시
									
									sleep(1000);//1초마다 작동
					} catch (InterruptedException e) {
									e.printStackTrace();
					}
			} //- while
		}
		
	} 
  //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
}
