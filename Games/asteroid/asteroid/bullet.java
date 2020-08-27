package asteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

class bullet {
	
	double x, y, r;
	double width, height;
	double xVel, yVel;
	boolean deadbul;
	
	boolean pow_shot;
	boolean pow_laz;
	boolean pow_frz;
	
	double rotation;
	
	
	public bullet(Turret t1) {
		x = t1.tx;
		y = t1.ty;
		r = 6;
		width = 6;
		height = 6;
		xVel = (t1.utx-t1.bx)*0.50;
		yVel = (t1.uty-t1.by)*0.50;
		deadbul = false;
		
		rotation = 0;
		
		setSpeed (0.05, 0.05);
	}
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(rotation), x, y);
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
		//g2d.setTransform(at);
		if(pow_shot) {
			g2d.setColor(Color.PINK);
			g2d.fillOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
		}
		else if (pow_laz) {
			g2d.setColor(Color.BLUE);
			g2d.fillOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
		}
		else if(pow_frz) {
			g2d.setTransform(at);
			g2d.setColor(Color.CYAN);
			g2d.drawRect((int)(x),(int) (y-r), (int)0, (int) r*2);
			g2d.drawRect((int)(x-r),(int) (y), (int)r*2, (int) 0);
			at.rotate(Math.toRadians(rotation+45), x, y);
			g2d.setTransform(at);
			g2d.drawRect((int)(x),(int) (y-r), (int)0, (int) r*2);
			g2d.drawRect((int)(x-r),(int) (y), (int)r*2, (int) 0);
			rotation=rotation+15;
			if(rotation>360) {
				rotation=rotation-360;
			}
			g2d.setTransform(new AffineTransform());
			//g2d.drawOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
		}
		else {
			g2d.setColor(Color.GREEN);
			g2d.fillOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
		}
		
		//g2d.fillOval((int)(x-r), (int)(y-r), (int)r*2, (int)r*2);
					
	}
	
	public bullet setRadius(double radius2) {
		this.r=radius2;
		return this;
		
	}
	
	public bullet setSpeed (double xVelModifier, double yVelModifier) {
		this.xVel = this.xVel*xVelModifier;
		this.yVel = this.yVel*yVelModifier;
		return this;
	}
	
	
	public  bullet Shotgun () {
		this.pow_shot = true;
		this.pow_laz = false;
		this.pow_frz = false;
		return this;
	}
	public  bullet Lazer () {
		this.pow_shot = false;
		this.pow_laz = true;
		this.pow_frz = false;
		return this;
	}
	public  bullet Freeze () {
		this.pow_shot = false;
		this.pow_laz = false;
		this.pow_frz = true;
		return this;
	}
	public bullet noPower() {
		this.pow_shot = false;
		this.pow_laz = false;
		this.pow_frz = false;
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
