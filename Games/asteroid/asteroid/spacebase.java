package asteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class spacebase {
	double x, y, radius;
	
	public spacebase() {
		x=640;
		y=360;
		radius=50;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(0), x, y);
		
		g2d.setColor(Color.gray);
		g2d.setTransform(at);
		g2d.fillOval((int)(x-radius), (int)(y-radius),(int) radius*2,(int) radius*2);
		
	}

}
