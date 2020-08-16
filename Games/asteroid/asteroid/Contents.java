package asteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import asteroid.Turret;
import asteroid.bullet;
import asteroid.rock;

public class Contents extends JPanel implements KeyListener, ActionListener {
	
	private Timer timer;
	private int delay = 8;
	
	Wall w1 = new Wall();
	spacebase b1 = new spacebase();
	Turret t1 = new Turret();
	
	boolean bullethold;
	boolean bulletspray;
	
	ArrayList<rock> rocks = new ArrayList<rock>();
	ArrayList<bullet> bullets = new ArrayList<bullet>();
	int spawntimer = 1000;
	int bulletspawntimer = 0;
	
	boolean gameStart;
	boolean gameOver;
	//boolean restart;
	
	int lifeTotal;
	int score;
	
	
	public Contents() {
		rocks.add(new rock());
		super.setDoubleBuffered(true);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
		bullethold = false;
		bulletspray = false;
		gameStart = false;
		gameOver = false;
		//restart = false;
		lifeTotal = 10;
		score = 0;
	}
	
	
	//This is where objects are drawn to the JFRAME
	@Override
	public void paintComponent(Graphics g) {
		//background
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, 1280, 720);
		

		if(gameOver==false && gameStart==false){
			g2d.setColor(Color.white);
			g2d.drawString("PRESS 'Enter' TO START", 570, 250);
			t1.draw(g2d);
			w1.draw(g2d);
			b1.draw(g2d);
			//w1.r=w1.r+1;
			//w1.move();
		}
		//GAME OVER
		else if(gameOver==true && gameStart == true) {
			g2d.setColor(Color.white);
			g2d.drawString("GAME OVER", 620, 345);
			g2d.setColor(Color.white);
			g2d.drawString("SCORE: " + String.valueOf(score), 622, 360);
			g2d.drawString("PRESS 'ENTER' TO RESTART", 570, 375);
			//timer.stop();
		}
		else if(gameOver==false && gameStart==true) {
			
			//game pieces
			t1.draw(g2d);
			//g2d.setTransform(new AffineTransform());
			w1.draw(g2d);
			//anything placed beneath the base b1 seems to have connected movements
			//put player controlled objects above b1, and the rest underneath
			//"fix" by setting g2d.setTansform to 0
			b1.draw(g2d);
			//GAME START
			if(gameStart) {
		
				//rock-base collision iterator
				//life deduction on collision
				g2d.setColor(Color.red); g2d.drawString("Hull Integrity: ", 10, 15);
				g2d.setColor(Color.green); g2d.drawString(String.valueOf(lifeTotal), 83, 15);
				Iterator<rock> itrr = rocks.iterator();
				while(itrr.hasNext()) {
					rock currRock = itrr.next();
					if (currRock.spaceCollision(b1)) {
						if(currRock.radius == 30) {
							lifeTotal = lifeTotal -3;
						}
						if(currRock.radius == 20) {
							lifeTotal = lifeTotal -2;
						}
						if(currRock.radius == 10) {
							lifeTotal = lifeTotal -1;
						}
						itrr.remove();
					}
				}
				
				if(lifeTotal < 1) {
					gameOver = true;
				}
				
				//hiddenCalc shows the rocks relation to the wall if both were rotated to 0 degrees, easier for calculations
				for(int i = 0; i<rocks.size();i++) {
					rocks.get(i).draw(g2d);
					rocks.get(i).hiddenCalc(g2d,w1);
					rocks.get(i).wallCollision(w1);
				}
				
				//g2d.setTransform(new AffineTransform());;
				
				
				//draw bullets and set time for next bullet
				//draw text to show cooldown of next shot
				//becomes useful when new bullet types are added
				if(bulletspawntimer!=0) {
					bulletspawntimer = bulletspawntimer -1;
					g2d.setColor(Color.red);
					g2d.drawString("Turret Cooldown: "+String.valueOf(bulletspawntimer), 10, 30);
				}
				else {
					g2d.setColor(Color.red); g2d.drawString("Turret Cooldown:", 10, 30);
					g2d.setColor(Color.green); g2d.drawString("READY", 107,30);
				}
				for(int i = 0; i<bullets.size();i++) {
					bullets.get(i).draw(g2d);
				}
				
				//rock spawn rate
				if(spawntimer != 0) {
					spawntimer = spawntimer -1;
				}
				else {
					rocks.add(new rock());
					spawntimer = (int) Math.round(Math.random() * 1000 + 400) ;
				}
				
				
				
				//bullet-end iterator
				Iterator<bullet> itrb = bullets.iterator();
				while(itrb.hasNext()) {
					bullet currBull = itrb.next();
					if(currBull.deadbul) {
						itrb.remove();
					}
				}
				
				//rock-bullet collision iterator
				ArrayList <rock> addAfter = new ArrayList<rock>();
				itrb = bullets.iterator();
				while(itrb.hasNext()) {
					itrr = rocks.iterator();
					bullet currBull = itrb.next();
					while(itrr.hasNext()) {
						rock currRock = itrr.next();
						if( currRock.bulletCollision(currBull)) {
							if(currRock.radius==30) {
								addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
								addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
							}
							if(currRock.radius==20) {
								addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
								addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
							}
							itrr.remove();
							itrb.remove();
							score=score+5;
							break;
						}
					}
				}
				//adds new rocks
				for(int i = 0 ; i<addAfter.size();i++) {
					rocks.add(addAfter.get(i));
				}
				
				//display score
				g2d.setColor(Color.red);
				g2d.drawString("Score: ", 10, 45);
				g2d.setColor(Color.green);
				g2d.drawString(String.valueOf(score), 48, 45);
				
				
			//gamestart bracket
			}
		}
		
		
		
		//TEST ZONE FOR PAINTCOMPONENT
		//TRY TO KEEP ALL DRAWING HERE FOR EASIER CLEANUP
		
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		timer.start();
		
		
		if(gameStart) {
			//rock mover
			for(int i = 0; i<rocks.size();i++) {
				rocks.get(i).move();
			}
			//bullet mover
			for(int i = 0; i<bullets.size();i++) {
				bullets.get(i).move();
			}
	
			//bullet firerate
			if(bullethold) {
				if(bulletspawntimer<1) {
					//shotgun powerup, currently toggable
					if(bulletspray) {
						int numberOfBullets = (int) Math.round(8);
						for(int i=0; i<numberOfBullets; i++) {
							bullet pellet = new bullet(t1).setRadius(3).setSpeed(0.05, 0.05);
							int decider = (int) Math.round(Math.random()*3+1);
							if(decider == 1) {
								pellet.xVel = pellet.xVel + ((Math.random()*0.75));
								pellet.yVel = pellet.yVel + ((Math.random()*0.75));
							}
							if(decider == 2) {
								pellet.xVel = pellet.xVel + ((Math.random()*0.75));
								pellet.yVel = pellet.yVel - ((Math.random()*0.75));
							}
							if(decider == 3) {
								pellet.xVel = pellet.xVel - ((Math.random()*0.75));
								pellet.yVel = pellet.yVel + ((Math.random()*0.75));
							}
							if(decider == 4) {
								pellet.xVel = pellet.xVel - ((Math.random()*0.75));
								pellet.yVel = pellet.yVel - ((Math.random()*0.75));
							}
							bullets.add(pellet);
						}
						bulletspawntimer = 200;
					}
					else {
						bullets.add(new bullet(t1).setSpeed(0.05, 0.05));
						bulletspawntimer=40;
					}
				}
			}
		}
		w1.move();
		t1.move();
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameStart) {
			//WALL CONTROLS
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				w1.setclockwise(true);
				w1.movingWall = -1;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				w1.setcounterclockwise(true);
				w1.movingWall = -1;
			}
			
			//TURRET CONTROLS
			if(e.getKeyCode() == KeyEvent.VK_D) {
				t1.setclockwise(true);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A) {
				t1.setcounterclockwise(true);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				bullethold = true;
				
			}
			
			//shotgun toggle
			if(e.getKeyCode() == KeyEvent.VK_S) {
				bulletspray = !bulletspray;
			}
		}
		
		//START GAME
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameStart==false && gameOver == false) {
				gameStart = true;
			}
			if(gameOver == true && gameStart == true) {
				score = 0;
				lifeTotal = 10;
				w1.restart();
				t1.restart();

				rocks.removeAll(rocks);
				bullets.removeAll(bullets);
				rocks.add(new rock());
				spawntimer = 1000;
				bulletspray = false;
				gameOver = false;
				gameStart = false;
			}
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			w1.setclockwise(false);
			w1.movingWall = 1;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			w1.setcounterclockwise(false);
			w1.movingWall = 1;
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			t1.setclockwise(false);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_A) {
			t1.setcounterclockwise(false);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			bullethold = false;
		}
	}
	

	
}
