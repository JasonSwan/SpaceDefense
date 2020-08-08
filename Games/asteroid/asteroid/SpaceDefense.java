package asteroid;

//import java.applet.Applet;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class SpaceDefense extends JFrame {
	final int WIDTH = 1280, HEIGHT = 720;
	
	JFrame frame;
	BufferedImage bf;
	
	public SpaceDefense() {
		
		bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		super.setTitle("Space Defense");
		super.setSize(WIDTH, HEIGHT);
		super.setLocation(360,150);
		super.setResizable(false);
		super.setAlwaysOnTop(true);
		super.add(new Contents() );
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(true);
		
	}

	public static void main(String[] args) {
		new SpaceDefense();
	}
	
}
