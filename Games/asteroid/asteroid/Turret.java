package asteroid;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Turret {
	double x, y, r;
	boolean counterclockwise, clockwise;
	double bx, by;
	double tx, ty;
	
	double startx, starty;
	
	public Turret() {
		x=640;
		y=300;
		r=0;
		
		bx=640;
		by=310;
		
		tx = 640;
		ty = 280;
		
		startx = 640;
		starty = 300;
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(r), x, y);
		
		
		g2d.setColor(Color.gray);
		g2d.setTransform(at);
		g2d.fillRect((int)x-5, (int)y-20, 10, 25);
		
		//test pixel points
		/*
		g2d.setColor(Color.red);
		g2d.setTransform(at);
		g2d.drawRect((int)x , (int)y-10, 1 ,1);
		
		AffineTransform ats = new AffineTransform();
		at.rotate(Math.toRadians(r), bx, by);
		
		g2d.setColor(Color.red);
		g2d.setTransform(ats);
		g2d.drawRect((int)bx , (int)by, 1 ,1);
		
		g2d.setColor(Color.red);
		g2d.setTransform(ats);
		g2d.drawRect((int)tx , (int)ty, 1 ,1);
		*/
	
	}
	
	public void move() {
		if(clockwise) {
			r=r+1.5;
			if(r>=360) {
				r=r-360;
			}
			x =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((300-360)*Math.sin((Math.toRadians(r))) - 640) );
			y =Math.round(  (300-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			bx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((310-360)*Math.sin((Math.toRadians(r))) - 640) );
			by =Math.round(  (310-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			tx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((280-360)*Math.sin((Math.toRadians(r))) - 640) );
			ty =Math.round(  (280-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
		
		}
		if(counterclockwise) {
			r=r-1.5;
			if(r<=-360) {
				r=r+360;
			}
			x =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((300-360)*Math.sin((Math.toRadians(r))) - 640) );
			y =Math.round(  (300-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			bx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((310-360)*Math.sin((Math.toRadians(r))) - 640) );
			by =Math.round(  (310-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			tx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((280-360)*Math.sin((Math.toRadians(r))) - 640) );
			ty =Math.round(  (280-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
		
		}
	}

	public void setclockwise (boolean input) {
		clockwise = input;
	}
	
	public void setcounterclockwise (boolean input) {
		counterclockwise = input;
	}
	
	public void restart() {
		r = 0;
		x = startx;
		y = starty;
		bx=640;
		by=310;
		tx = 640;
		ty = 280;
		
	}


}