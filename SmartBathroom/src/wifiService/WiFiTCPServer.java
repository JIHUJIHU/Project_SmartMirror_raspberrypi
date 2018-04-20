package wifiService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import commData.CommData;

public class WiFiTCPServer extends Thread{
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String 		TAG					=	"WiFiTCPServerThread";
	public  ServerSocket 							serverSocket;
	public  Connection								connThread = null;
	//private static InputStream 			mmInStream;
	//private static OutputStream 		mmOutStream;
	
	
	//static String[] str;  
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public WiFiTCPServer(){
    	try
    		{
    			this.serverSocket = new ServerSocket(3000);
	    }catch (Exception e) {
	    		CommData.log(TAG, "Exception : " + e.getMessage());
	   		}
    }

   
	@Override
  public void run() {
	
			try {
							CommData.log(TAG, "Wating Connect ..");
							Socket clientSock = this.serverSocket.accept();
							if(clientSock != null) {
								this.connThread = new Connection(clientSock);
								this.connThread.start();
							}
						
		  } catch (IOException e) {
						//e.printStackTrace();
		   }  
	   	  
   }


 public void stopThread(){
	  
	  if(this.connThread != null){
		  this.connThread.interrupt();
		  this.connThread = null;
		  CommData.log(TAG+" - CheckSensor THREAD : ", " STOP ");
	     }
    }
 
 
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Client 접속 처리 Thread
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Connection extends Thread{ 

   private final Socket       			clientSocket;
   private final InputStream    		mmInStream;
   private final OutputStream   		mmOutStream;

	 //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 //- Constructor Method
	  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   public Connection (Socket clientSocket){ 

         this.clientSocket = clientSocket; 
         InputStream tmpIn = null;
         OutputStream tmpOut = null;
            
         try {
               tmpIn = clientSocket.getInputStream();
               tmpOut = clientSocket.getOutputStream();
         } catch (IOException e) {
        	      CommData.log(TAG, "Connection() :"+e.getMessage());
         		  }
            
         mmInStream = tmpIn;
         mmOutStream = tmpOut;
   	  } 

	 //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 //- Override Method
	  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   public void run(){
	
        byte[] 		buffer = new byte[230400];
        boolean isStart = false;
        boolean isImage = false;
        byte head = 0;
        byte bData = 0;
        byte bData2 = 0;
        byte bData3 = 0;
        int nCnt = 0;
        		
        CommData.log(TAG, "WiFi 연결됨");
        
        // Keep listening to the InputStream while connected
        while(true)
        		{
            try{
            			// Header check
            			if(!isStart)
               						{
		                  head = (byte) mmInStream.read();
		                  CommData.log(TAG, "head:"+(char)head);
		                  
		                  if(head == '@') {
		                     isStart = true;
		                     for(int i=0; i < 10; i++) { buffer[i] = 0; }
                  						   }
                  }else{
                	  bData = (byte) mmInStream.read();
                  
                	  if(bData == '#'){
	                     //- Image Data 수신....... 처리 
	                     if(isImage){
	                    	  CommData.log(TAG, "isImage -1 ");
	                        bData2 = (byte) mmInStream.read();
	                        
	                        if(bData2 == '#'){
	                           bData3 = (byte) mmInStream.read();
	                           CommData.log(TAG, "isImage -2 ");
	                           
	                           if(bData3 == '#'){
	                        	    CommData.log(TAG, "IsImage -3");
	                        	   								//- 수신한 이미지 데이터를 이미지 파일로 생성"toothGif/T"
	                        	    File file = new File("image/test_1226.jpg");
	                        	    FileOutputStream fos = new FileOutputStream(file);
	                        	    for(int i=4; i < nCnt; i++) fos.write(buffer[i]);   //- 파일에 쓰기
	                        	    							
	                        	    fos.flush();
	                        	    fos.close();
	                        	    isImage = false;
	                        	    isStart = false;
	                        	    nCnt = 0;
	                            } else {
	                               buffer[nCnt++] = bData;
	                               buffer[nCnt++] = bData2;
	                               buffer[nCnt++] = bData3;
	                            							}
	                        } else {
	                           buffer[nCnt++] = bData;
	                           buffer[nCnt++] = bData2;
	                        						}
	                     }/*else{
	                        //- Door Open & Close Command  @MOT,OPEN# / @MOT,CLOSE#
	                        byte[] sendBuffer = new byte[20];
	                        for(int i=0; i < nCnt; i++) {
	                           sendBuffer[i] = buffer[i];
	                        						}
	                        CommData.log(TAG, "RX DATA = "+ new String(sendBuffer).trim());
	                        isStart = false;
	                        nCnt = 0;
	                     					}*/
	                     
                	}else{	
                     buffer[nCnt++] = bData;
                     if(nCnt == 3) {
                        String strHead = new String(buffer, 0, 3);
                        if("IMG".equals(strHead))
                        		isImage =true;
                        else
                        		isImage = false;
                     						}
                  					}
                  
                  				}
            			       /*
            			       //- 음성인식에 대한 결과 Client에게 전송
            	  if(CommData.gVOICE_RESULT.indexOf(CommData.OPEN)>=0)
            		  	mmOutStream.write(CommData.DOOR_OPEN.getBytes());
            	  else
            		    mmOutStream.write(CommData.DOOR_CLOSE.getBytes());
            			    	  */
            }catch (IOException e){
                CommData.log(TAG, "Disconnect = "+e.getMessage());
                break;
            			}
            
        } //- while
        
   	} //- run()

	} //- Connection class

}