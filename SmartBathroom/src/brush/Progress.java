package brush;

public class Progress extends Thread{
	public static int Pcount= 0;
	
	public void toggleProgress(boolean bool) {
		if(bool) {
			BTPanel.progresspanel.setVisible(true);
		}
		else {
			BTPanel.progresspanel.setVisible(false);
		}
	}
	
	public void setProgressBasicSet() {
		BTPanel.progresspanel.setVisible(true);
		toggleProgress(true);
		Pcount=0;
	}
	
	public void run(){
		setProgressBasicSet();
	    while(Pcount<=100) {
	    	BTPanel.progressBar.setValue(Pcount);
	    	Pcount++;
	    	
	    	if(Pcount==1)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[1]);
	    	if(Pcount==10)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[2]);
	    	if(Pcount==20)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[3]);
	    	if(Pcount==30)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[4]);
	    	if(Pcount==40)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[5]);
	    	if(Pcount==50)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[6]);
	    	if(Pcount==60)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[7]);
	    	if(Pcount==70)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[8]);
	    	if(Pcount==80)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[9]);
	    	if(Pcount==90)
	    		BTPanel.imageLabel.setIcon(BTPanel.ic[10]);
	    	
	    	if(Pcount>=100) {
	    		BTPanel.progresspanel.setVisible(false);
	    	}
	    	try {
				Progress.sleep(500);//1800->3minute
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	}
	
}
