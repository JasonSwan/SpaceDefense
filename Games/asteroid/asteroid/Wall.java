package asteroid;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Wall {
	//center point and rotation degree
	double x, y, r;
	//top corners
	public double ltx;
	double lty;
	public double rtx;
	double rty;
	//middle top edges
	double mtx, mty;
	//midpoint side edges
	double lmx,rmx;
	//bottom corners
	double lbx, lby, rbx, rby;
	
	double width, height;
	
	
	double xcalc, ycalc;
	
	int movingWall;
	
	double startx, starty;
	
	boolean counterclockwise, clockwise;
	
	public Wall() {
		x=640;
		y=300;
		r=0;
		
		width = 100;
		height = 10;
		
		mtx = 640;
		mty = 295;
		
		xcalc = 640;
		ycalc = 300;
		
		ltx = 590;lty= 295;
		rtx = 690;rty= 295;
		
		lbx = 590;lby= 305;
		rbx = 690;rby= 305;
		
		movingWall = 1;
		
		startx = 640;
		starty = 300;
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(r), x, y);
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
		g2d.setColor(Color.cyan);
		g2d.setTransform(at);
		g2d.fillRect((int)x-50, (int)y-5, (int)width, (int)height);
					
	}
	
	public void move() {
		if(clockwise) {
			r=r+1;
			if(r==360) {
				r=0;
			}

			x =( (0)*Math.cos(((Math.toRadians(r))))  - ((300-360)*Math.sin((Math.toRadians(r))) - 640) );
			y =Math.round(  (300-360)*Math.cos(((Math.toRadians(r)))) + (0)*Math.sin((Math.toRadians(r)))+360 );
			
		}
		if(counterclockwise) {
			r=r-1;
			if(r==-360) {
				r=0;
			}
			x =Math.round( (0)*Math.cos(((Math.toRadians(r))))  - ((300-360)*Math.sin((Math.toRadians(r))) - 640) );
			y =Math.round(  (300-360)*Math.cos(((Math.toRadians(r)))) + (0)*Math.sin((Math.toRadians(r)))+360 );
			
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
	}

}
