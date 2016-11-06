package Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

import Display.DisplayFrame;
import LeapMouse.LeapMouseListener;

public class Main {

	public static void main(String[] args) {
		Properties prop = getProperties("app.cfg");
		
		//get box coordinate settings
		Vector max = new Vector(Integer.parseInt(prop.getProperty("max-x")), Integer.parseInt(prop.getProperty("max-y")), Integer.parseInt(prop.getProperty("max-z")));
		Vector min = new Vector(Integer.parseInt(prop.getProperty("min-x")), Integer.parseInt(prop.getProperty("min-y")), Integer.parseInt(prop.getProperty("min-z")));

		//create listener and attach
		LeapMouseListener leapListener = new LeapMouseListener(max, min);
		Controller controller = new Controller();
		controller.addListener(leapListener);
		
		//create GUI and set settings
		DisplayFrame gui = new DisplayFrame();
		gui.setTitle(prop.getProperty("window-title"));
		gui.setSize(Integer.parseInt(prop.getProperty("window-width")), Integer.parseInt(prop.getProperty("window-height")));
		gui.setVisible(true);
		if(prop.getProperty("start-fullscreen").compareTo("true") == 0) gui.setExtendedState(JFrame.MAXIMIZED_BOTH); 

		//create GUI icon
		BufferedImage icon;
		try {
			icon = ImageIO.read(new File("ivg.png"));
			gui.setIconImage(icon);
		} catch (Exception e) {}

		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(leapListener);
	}

	//return properties object from file
	private static Properties getProperties(String file){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(file);
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not load properties file.");
			System.exit(0);
		}

		return prop;
	}
}
