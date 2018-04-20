package brush;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BTPanel extends JPanel{
	  
	public static JPanel progresspanel;
	public static JProgressBar progressBar;
	public static JLabel imageLabel;
  public static ImageIcon[] ic = new ImageIcon[11];

  public BTPanel() {
		SetImageIcon();
		progresspanel = new JPanel();
		progresspanel.setLayout(new BorderLayout());
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		add(progresspanel);
	  progressBar.setStringPainted(true);
	  progressBar.setBorderPainted(false);
	  progressBar.setStringPainted(false);
	  //progressBar.setBackground(Color.BLACK);
	  progressBar.setForeground(Color.BLACK);
	  	
	  imageLabel = new JLabel(ic[1]);
	  	
	  progresspanel.add(imageLabel,BorderLayout.SOUTH);
	  progresspanel.add(progressBar,BorderLayout.NORTH);
	  progresspanel.setVisible(false);
}
  
  private void SetImageIcon() {
	  String PathStr = null;
	  for(int i=1; i<=10; i++) {
	  	PathStr = "toothGif/T"+i+".gif";
	  	BTPanel.ic[i] = new ImageIcon(PathStr);
	  	}
  	}
  	
  
}


