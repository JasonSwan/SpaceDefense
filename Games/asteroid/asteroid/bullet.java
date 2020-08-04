package asteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

class bullet {
	
	double x, y, r;
	double width, height;
	double xVel, yVel;
	boolean deadbul;
	
	
	public bullet(Turret t1) {
		x = t1.tx;
		y = t1.ty;
		r = 6;
		width = 6;
		height = 6;
		xVel = (t1.x-t1.bx)*0.50;
		yVel = (t1.y-t1.by)*0.50;
		deadbul = false;
	}
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//AffineTransform at = new AffineTransform();
		//at.rotate(Math.toRadians(r), 640, 360);
		/*
		g2d.fillRect((int) ltx, (int)lty, 1, 1);
		g2d.fillRect((int) rtx,(int) rty ,1, 1);
		g2d.fillRect((int) lbx, (int)lby, 1, 1);
		g2d.fillRect((int) rbx,(int) rby ,1, 1);
		*/
		/*
		g2d.setColor(Color.green);
		g2d.drawRect(640-50, 300-5, (int)width, (int)height); 
		*/
		g2d.setColor(Color.green);
		//g2d.setTransform(at);
		g2d.fillOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
					
	}
	
	public bullet setRadius(double radius2) {
		this.r=radius2;
		return this;
		
	}
	
	
	public void move() {
		if(x<0 || x>1280) {
			deadbul = true;
		}
		
		if (y<0 || y>720) {
			deadbul = true;
		}
		
		
		x=x+xVel;
		y=y+yVel;
	}
	
	
}
