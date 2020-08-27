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
	double utx, uty;
	
	double startx, starty;
	
	boolean pow_shot;
	boolean pow_laz;
	boolean pow_frz;
	
	double lazx1; double lazx2; double lazy;
	
	public Turret() {
		x=640;
		y=300;
		r=0;
		
		bx=640;
		by=310;
		
		tx = 640;
		ty = 280;
		
		
		utx = 640;
		uty = 50;
		
		startx = 640;
		starty = 300;
		
		
		pow_shot = false;
		pow_laz = false;
		pow_frz = false;
		
		lazx1 = 638; lazx2 = 641; lazy = -780;
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(r), x, y);
		
		
		g2d.setColor(Color.gray);
		g2d.setTransform(at);
		g2d.fillRect((int)x-5, (int)y-20, 10, 25);
		
		//g2d.setColor(Color.RED);
		//g2d.drawRect((int)x-2, (int)y-20, 0, 780);
		//g2d.drawRect((int)x+1, (int)y-20, 0, 780);
		
		//g2d.setColor(Color.RED);
		//g2d.drawRect((int)lazx1, (int)lazy, 0, 1060);
		//g2d.drawRect((int)lazx2, (int)lazy, 0, 1060);
		
		//AffineTransform ats = new AffineTransform();
		if(pow_shot) {
			g2d.setColor(Color.PINK);
		}
		else if (pow_laz) {
			g2d.setColor(Color.BLUE);
		}
		else if(pow_frz) {
			g2d.setColor(Color.CYAN);
		}
		else {
			g2d.setColor(Color.GREEN);
		}
		g2d.fillRect((int)x-1 , (int)y-16, 2 ,16);
		
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
	
	
	
	public  Turret Shotgun () {
		this.pow_shot = true;
		this.pow_laz = false;
		this.pow_frz = false;
		return this;
	}
	public  Turret Lazer () {
		this.pow_shot = false;
		this.pow_laz = true;
		this.pow_frz = false;
		return this;
	}
	public  Turret Freeze () {
		this.pow_shot = false;
		this.pow_laz = false;
		this.pow_frz = true;
		return this;
	}
	public Turret noPower() {
		this.pow_shot = false;
		this.pow_laz = false;
		this.pow_frz = false;
		return this;
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
		
			utx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((50-360)*Math.sin((Math.toRadians(r))) - 640) );
			uty =Math.round(  (50-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			
			//lazx1 = Math.round( (638-640)*Math.cos(((Math.toRadians(r))))  - ((360-360)*Math.sin((Math.toRadians(r))) - 640) );
			//lazx2 = Math.round( (641-640)*Math.cos(((Math.toRadians(r))))  - ((360-360)*Math.sin((Math.toRadians(r))) - 640) );
			//lazy = Math.round(  (-780-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			//lazy = Math.round(  (50-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
			
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
			
			utx =Math.round( (640-640)*Math.cos(((Math.toRadians(r))))  - ((50-360)*Math.sin((Math.toRadians(r))) - 640) );
			uty =Math.round(  (50-360)*Math.cos(((Math.toRadians(r)))) + (640-640)*Math.sin((Math.toRadians(r)))+360 );
		
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
		
		utx = 640;
		uty = 50;
		
	}


}