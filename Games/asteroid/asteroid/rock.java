package asteroid;

import java.awt.Color;
import java.awt.Graphics;


class rock {
	double xVel, yVel, x ,y, radius;
	double xrecent,yrecent;
	double xcalc, ycalc;
	double xprev,yprev;
	int collisionTimer;
	
	//rock power-ups
	boolean no_pow;
	boolean pow_shot;
	boolean pow_laz;
	boolean pow_frz;
	boolean pow_health;
	
	
	public rock(){
		//570 is left most side of base
		
		
		//make rocks spawn on random edge of map
		int spawner = (int) Math.round(Math.random()*4+1);
		if(spawner==1){
			x = Math.random() * 1270+10;
			y = 0;
		}
		else if(spawner==2) {
			x = Math.random() * 1270+10;
			y = 710;
		}
		else if(spawner==3) {
			x=0;
			y = Math.random() * 710 + 10;
		}
		else if(spawner==4) {
			x=1270;
			y = Math.random() * 710 + 10;
		}
		
		

		xVel = getRandomSpeed()*getRandomDirection();
		yVel = getRandomSpeed()*getRandomDirection();
		
		
		radius = 30;
		
		/*
		x = 727;
		
		y = 0;
		xVel = -0.5;
		yVel = 1.0;
		*/
		
		xcalc=x;
		ycalc=y;
		
		
		xrecent = x;
		yrecent = y;
		
		setPower();
		
	}
	
	public rock setRadius(double radius2) {
		this.radius=radius2;
		return this;
		
	}
	
	public rock setSpawn (int xloc, int yloc) {
		this.x = xloc;
		this.y = yloc;
		return this;
	}
	
	public double getRandomSpeed() {
		return(Math.random() *(2-0.20)+0.20);
	}
	
	public int getRandomDirection() {
		int rand = (int) (Math.random() *2);
		if(rand==1) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	public rock setSpeed (double xVelModifier, double yVelModifier) {
		this.xVel = this.xVel*xVelModifier;
		this.yVel = this.yVel*yVelModifier;
		return this;
	}
	public void setPower() {
		int power = (int) Math.abs((Math.random()*35));
		if(power==0) {
			pow_shot=true;
		}
		else if (power==1) {
			pow_laz=true;
		}
		else if (power==2) {
			pow_frz=true;
		}
		else if (power==3) {
			pow_health=true;
		}
		else {
			no_pow=true;
		}
	}
	
	public void draw(Graphics g) {
		if(no_pow) {
			g.setColor(Color.ORANGE);
			g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
		}
		else if(pow_shot) {
			g.setColor(Color.PINK);
			g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
		}
		else if(pow_laz) {
			g.setColor(Color.BLUE);
			g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
		}
		else if(pow_frz) {
			g.setColor(Color.CYAN);
			g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
		}
		else if (pow_health) {
			g.setColor(Color.RED);
			g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
		}
		//g.setColor(Color.orange);
		//g.drawOval((int)(xrecent-(radius)), (int)(yrecent-(radius)), (int)(radius*2), (int)(2*radius));
	}
	
	//test to record hidden rock point
	//alter to for collision
	public void hiddenCalc (Graphics g, Wall w1) {
		/*
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(-w1.r), 640, 360);
		g2d.setTransform(at);
		g2d.setColor(Color.green);
		g2d.drawOval((int)(xcalc-radius), (int)(ycalc-radius), (int)radius*2, (int)radius*2);
		*/
		xcalc = ( (x-640)*Math.cos(((Math.toRadians(-w1.r)))) - ((y-360)*Math.sin((Math.toRadians(-w1.r))) - 640) );
		ycalc = ( (y-360)*Math.cos(((Math.toRadians(-w1.r)))) + (x-640)*Math.sin((Math.toRadians(-w1.r)))+ 360 );
		
	}
	
	
	//FIX COLLISION TIMER BY SETTING ROCKS TO INTANGIBLE UNTIL BULLET COLLISION IS FALSE
	//attempting by collision moving rock back to xprev/yprev coordinates
	
	//if closest x is greater than ltx but less than half of radius, bounce at half radian angle
	//need to add extra if else to both x and y
	public boolean wallCollision (Wall w) {
		if (collisionTimer!=0) {
			return false;
		}
		
		//testing stretched margins
		//remove -1/+1 if wrong
		int[] ans = new int[2];
		ans[0] = 0;
		if (xcalc < w.ltx-(radius/2)) {
			int closestx = (int) w.ltx;
			ans[0] = closestx;
		}
		else if (xcalc > w.rtx+(radius/2)) {
			int closestx = (int) w.rtx;
			ans[0] = closestx;
		}
		else {
			int closestx = (int) xcalc;
			ans[0] = closestx;
		}
		
		if (ycalc < w.lty-(radius/2)) {
			int closesty = (int) w.lty;
			ans[1] = closesty;
		}
		else if (ycalc > w.lby+(radius/2)) {
			int closesty = (int) w.lby;
			ans[1] = closesty;
		}
		else {
			int closesty = (int) ycalc;
			ans[1] = closesty;
		}
		///////////////////////////////////
		///////////////////////////////////
		///////////////////////////////////
		int total = (int) findDistance(ans[0],ans[1],xcalc,ycalc);
		
		if(total < (radius)) {
			
			double newxVel;
			double newyVel;
			
			
			
			newxVel = Math.cos(Math.toRadians(w.r))*(xcalc-xprev) + Math.sin(Math.toRadians(w.r))*(ycalc-yprev);
			newyVel = -Math.sin(Math.toRadians(w.r))*(xcalc-xprev) + Math.cos(Math.toRadians(w.r))*(ycalc-yprev);
	
			/*
			double newxVel = (xcalc-xprev);
			double newyVel = (ycalc-yprev);
			*/
			if(w.movingWall == -1) {
				newxVel =-1*(( (xcalc-640)*Math.cos(((Math.toRadians(w.r)))) - ((ycalc-360)*Math.sin((Math.toRadians(w.r))) - 640) ) -
						( (xprev-640)*Math.cos(((Math.toRadians(w.r)))) - ((yprev-360)*Math.sin((Math.toRadians(w.r))) - 640) ));
				newyVel =-1*(( (ycalc-360)*Math.cos(((Math.toRadians(w.r)))) + (xcalc-640)*Math.sin((Math.toRadians(w.r)))+ 360 ) - 
						( (yprev-360)*Math.cos(((Math.toRadians(w.r)))) + (xprev-640)*Math.sin((Math.toRadians(w.r)))+ 360 ));
			}
			
	
			
			if(ans[0] == w.ltx) {
				x = xrecent;
				y = yrecent;
				yVel=newyVel;
				xVel=w.movingWall*newxVel*-1;
			}
			else if (ans[0] == w.rtx) {
				x = xrecent;
				y = yrecent;
				yVel=newyVel;
				xVel=w.movingWall*newxVel*-1;
			}
			else if (ans[1]==w.lty) {
				x = xrecent;
				y = yrecent;
				yVel=w.movingWall*newyVel*-1;
				xVel=newxVel;
			}
			else if (ans[1]==w.lby) {
				x = xrecent;
				y = yrecent;
				yVel=w.movingWall*newyVel*-1;
				xVel=newxVel;
			}

			collisionTimer=100;
			return true;
		}
		
		return false;
	}
	
	public double findDistance(double fromX, double fromY, double toX, double toY){
	    double a = Math.abs(fromX - toX);
	    double b = Math.abs(fromY - toY);
	    return Math.sqrt((a * a) + (b * b));
	}
	
	public boolean spaceCollision(spacebase b ) {
		double xdif = Math.abs(b.x-x);
		double ydif = Math.abs(b.y-y);
		double distanceSquared = xdif * xdif + ydif * ydif;
		
		if(distanceSquared <( (b.radius+radius) * (b.radius + radius) ) ) {
			xVel=xVel * -1;
			yVel = yVel * -1;
		}
		return (distanceSquared <( (b.radius+radius) * (b.radius + radius) ) );
		
	}
	
	
	public boolean bulletCollision (bullet b) {
		
		double xdistance = Math.abs(x - b.x);
		double ydistance = Math.abs(y - b.y);
		
		double rsum = radius + b.r;
		double sqrrad = rsum * rsum;
		
		
		return (  ((xdistance*xdistance)+(ydistance*ydistance))<=sqrrad  );
	}
	
	
	public void move() {
		
		xrecent = x;
		yrecent = y;
		
		xprev = (xcalc);
		yprev = (ycalc);
		x+=xVel;
		y+=yVel;
		
		if(y<0) {
			y = 720;
		}
		if(y>720) {
			y = 0;
		}
		
		if(x<0) {
			x = 1280;
		}
		if(x>1280) {
			x = 0;
		}
		xcalc=x;
		ycalc=y;
		
		if (collisionTimer > 0) {
			collisionTimer = collisionTimer -1;
		}
		
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
}
