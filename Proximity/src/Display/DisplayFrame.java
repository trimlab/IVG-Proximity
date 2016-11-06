package Display;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Properties;

import javax.swing.JFrame;

public class DisplayFrame extends JFrame{

	public DisplayFrame(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("cursor.png");
		Cursor c = toolkit.createCustomCursor(image , new Point(0, 0), "img");
		setCursor (c);
		
	}
	
	public void setObject(Properties object){
		
	}
	
}
