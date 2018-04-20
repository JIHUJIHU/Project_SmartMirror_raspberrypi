package livingInformation;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import brush.BTPanel;

public class LivingInformationSouth extends JPanel{
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID 		= 1L;


	public LivingInformationSouth(){
	    this.setLayout(new BorderLayout(0, 0));
			this.setBackground(Color.BLACK);
			this.add(new BTPanel(),BorderLayout.EAST);
	}

}
