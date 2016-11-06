package Display;

import java.awt.Graphics;
import java.util.Properties;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel{
	
	private Properties object;
	
	public DisplayPanel(Properties object){
		this.object = object;
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
	}
}
