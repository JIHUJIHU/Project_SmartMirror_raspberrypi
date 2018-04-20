package livingInformation;

import javax.swing.JFrame;

import commData.CommData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;

import java.awt.Point;
import java.awt.Toolkit;

public class MainUI extends JFrame {
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String TAG 						 = "MainUI";
	private static final long serialVersionUID  = 1L;
  private LivingInformationNorth lvNorth;
  private LivingInformationSouth lvSouth;
  //	private static simpleSpeech speech;
//	private udpServer server;
	
//	public static void main(String[] args){
//		new mainUI(); //날씨, 시간정보를 제공하는 메인 UI
//	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public MainUI() {
		  getContentPane().setBackground(Color.BLACK);
	    getContentPane().setLayout(new BorderLayout(0, 	0));
	    
	        //커서 비활성화
	    Toolkit tk = Toolkit.getDefaultToolkit(); 
	    Cursor noCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
	    this.setCursor(noCursor);
	    	  
	    this.setUndecorated(true); 														// 상단바 제거
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);  					// 화면 최대화
	    this.setVisible(true); 																// 창이 보이게 하기.기본적으로는 보이지 않음
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		// 창 닫으면 프로그램 종료

	    	this.lvNorth = new LivingInformationNorth();
	    	this.lvSouth = new LivingInformationSouth();
	    	this.add(this.lvNorth, BorderLayout.NORTH);
		    this.add(this.lvSouth, BorderLayout.SOUTH);	
		    this.lvNorth.setVisible(false);
		    this.lvSouth.setVisible(false);
	    //this.add(new LivingInformationNorth(), BorderLayout.NORTH);
	    //this.add(new LivingInformationSouth(), BorderLayout.SOUTH);

//		speech = new simpleSpeech(); //음성인식 및 관련 스레드 실행
	    
	    CommData.log(TAG, "init OK");
	}	
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void setVisibletrue(){
				this.setVisible(true);
	}
	
	//- 날씨와  날짜 정보  출력 
	public void displayInfomation(){
	
	    //this.add(this.lvNorth, BorderLayout.NORTH);
	    //this.add(this.lvSouth, BorderLayout.SOUTH);		
		   this.lvNorth.setVisible(true);
		   this.lvSouth.setVisible(true);
	   
	}
	
	//- 날씨와  날짜 정보  출력 감추
	public void hiddenInfomation(){
		    
	   this.lvNorth.setVisible(false);
	   this.lvSouth.setVisible(false);
	}
}
