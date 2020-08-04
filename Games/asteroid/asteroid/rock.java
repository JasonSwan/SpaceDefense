package asteroid;

import java.awt.Color;
import java.awt.Graphics;

import asteroid.Wall;
import asteroid.spacebase;

class rock {
	double xVel, yVel, x ,y, radius;
	double xcalc, ycalc;
	double xprev,yprev;
	int collisionTimer;
	
	
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
		
		//x = 350;
		//y = 0;
		//xVel = 1.0;
		//yVel = 1.0;
		radius = 30;
		
		xcalc=x;
		ycalc=y;
		
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
		return(Math.random() *2);
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
	
	public void draw(Graphics g) {
		g.setColor(Color.orange);
		g.drawOval((int)(x-(radius)), (int)(y-(radius)), (int)(radius*2), (int)(2*radius));
		
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
	
	public boolean wallCollision (Wall w) {
		if (collisionTimer!=0) {
			return false;
		}
		int[] ans = new int[2];
		ans[0] = 0;
		if (xcalc < w.ltx) {
			int closestx = (int) w.ltx;
			ans[0] = closestx;
		}
		else if (xcalc > w.rtx) {
			int closestx = (int) w.rtx;
			ans[0] = closestx;
		}
		else {
			int closestx = (int) xcalc;
			ans[0] = closestx;
		}
		
		if (ycalc < w.lty) {
			int closesty = (int) w.lty;
			ans[1] = closesty;
		}
		else if (ycalc > w.lby) {
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
		
		//System.out.println("xcalc is: "+String.valueOf(xcalc));
		//System.out.println("xprev is: "+String.valueOf(xprev));
		//System.out.println("ycalc is: "+String.valueOf(ycalc));
		//System.out.println("yprev is: "+String.valueOf(yprev));
		
		if(total < (radius)) {
			
			double newxVel = Math.cos(Math.toRadians(w.r))*(xcalc-xprev) + Math.sin(Math.toRadians(w.r))*(ycalc-yprev);
			double newyVel = -Math.sin(Math.toRadians(w.r))*(xcalc-xprev) + Math.cos(Math.toRadians(w.r))*(ycalc-yprev);
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
			
			//System.out.println("newx is: " + String.valueOf(newxVel));
			//System.out.println("newy is; " + String.valueOf(newyVel));
			
			
			if(ans[0] == w.ltx) {
				yVel=newyVel;
				xVel=w.movingWall*newxVel*-1;
			}
			else if (ans[0] == w.rtx) {
				yVel=newyVel;
				xVel=w.movingWall*newxVel*-1;
			}
			else if (ans[1]==w.lty) {
				yVel=w.movingWall*newyVel*-1;
				xVel=newxVel;
			}
			else if (ans[1]==w.lby) {
				yVel=w.movingWall*newyVel*-1;
				xVel=newxVel;
			}
			
			//System.out.println("xvel is: " + String.valueOf(xVel));
			//System.out.println("yvel is; " + String.valueOf(yVel));
			
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
