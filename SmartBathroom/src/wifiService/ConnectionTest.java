package wifiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//------------------------------------------------------------------
//- Client 접속 처리 Thread
//------------------------------------------------------------------
class ConnectionTest extends Thread{ 

   //--------------------------------------------------------------
   //- Member Variable
   //--------------------------------------------------------------
   private final Socket       clientSocket;
   private final InputStream    mmInStream;
   private final OutputStream    mmOutStream;

 
   public ConnectionTest (Socket aClientSocket){ 

         clientSocket = aClientSocket; 
         InputStream tmpIn = null;
         OutputStream tmpOut = null;
            
         try {
               tmpIn = aClientSocket.getInputStream();
               tmpOut = aClientSocket.getOutputStream();
         } catch (IOException e) {
             System.out.println("Connection() :"+e.getMessage());
         }
            
         mmInStream = tmpIn;
         mmOutStream = tmpOut;
   } 


   public void run(){
	   System.out.println("연결됨");
        byte[] buffer = new byte[230400];
        boolean isStart = false;
        boolean isImage = false;
        byte head = 0;
        byte bData = 0;
        byte bData2 = 0;
        byte bData3 = 0;
        int nCnt = 0;

        // Keep listening to the InputStream while connected
        while(true)
        {
            try{
               // Header check
               if(!isStart)
               {
                  head = (byte) mmInStream.read();
                   System.out.println("head:"+(char)head);
                  if(head == '@') {
                     isStart = true;
                     for(int i=0; i < 10; i++) {
                        buffer[i] = 0;
                     }
                  }
               } else {
                  bData = (byte) mmInStream.read();
                  
                  if(bData == '#') {
                     //- Image Data 수신....... 처리 
                     if(isImage) {
                    	System.out.println("isImage -1 ");
                        bData2 = (byte) mmInStream.read();
                        if(bData2 == '#') {
                           bData3 = (byte) mmInStream.read();
                           System.out.println("isImage -2 ");
                           if(bData3 == '#') 
                           {
                    	   System.out.println("IsImage -3");
                           //- 수신한 이미지 데이터를 이미지 파일로 생성
                           File file = new File("G:\\image\\test_1226.jpg");
                           FileOutputStream fos = new FileOutputStream(file);
                           //byte[] sendBuffer = new byte[230400];
                           for(int i=0; i < nCnt; i++) {
                              //sendBuffer[i] = buffer[i];
                              fos.write(buffer[i]);
                           }
                           //- 파일에 쓰기
                           //fos.write(sendBuffer, 0, nCnt);
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
                     } else {
                        //- Door Open & Close Command  @MOT,OPEN# / @MOT,CLOSE#
                        byte[] sendBuffer = new byte[20];
                        for(int i=0; i < nCnt; i++) {
                           sendBuffer[i] = buffer[i];
                        }
                         
                        System.out.println("RX DATA = "+ new String(sendBuffer).trim());
                         isStart = false;
                         nCnt = 0;
                     }
                  } else {
                     buffer[nCnt++] = bData;
                     if(nCnt == 3) {
                        String strHead = new String(buffer, 0, 3);
                        if("IMG".equals(strHead)) {
                           isImage =true;
                           //- 이미지 수신 중.... 상태 표시
                        } else {
                           isImage = false;
                        }
                     }
                  }
                  
               }
            } catch (IOException e) {
               System.out.println("Disconnect = "+e.getMessage());
                break;
            }
        }
   }

}