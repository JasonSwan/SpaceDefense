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

//import asteroid.Turret;
//import asteroid.bullet;
//import asteroid.rock;

public class Contents extends JPanel implements KeyListener, ActionListener {
	
	private Timer timer;
	private int delay = 8;
	
	Wall w1 = new Wall();
	spacebase b1 = new spacebase();
	Turret t1 = new Turret();
	
	boolean bullethold;
	
	//power-up shots
	boolean pow_shot;
	boolean pow_laz;
	boolean pow_frz;
	
	ArrayList<rock> rocks = new ArrayList<rock>();
	ArrayList<bullet> bullets = new ArrayList<bullet>();
	int spawntimer = 1000;
	int bulletspawntimer = 0;
	int pow_timer;
	
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
		pow_shot = false;
		
		
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
				if(rocks.isEmpty()) {
					rocks.add(new rock());
				}
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
							//freeze shot
							if(currBull.pow_frz) {
								currRock.setSpeed(0.80, 0.80);
								itrb.remove();
								if(currRock.xVel < 0.05 && currRock.yVel < 0.05) {
									if(currRock.radius==30) {
										addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
										addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
									}
									if(currRock.radius==20) {
										addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
										addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
									}
									if(currRock.pow_health) {
										if(lifeTotal<10) {
											lifeTotal+=1;
										}
									}
									if(currRock.pow_shot) {
										pow_timer = 1800;
										t1.Shotgun();
									}
									if(currRock.pow_frz) {
										pow_timer = 1800;
										t1.Freeze();
									}
									if(currRock.pow_laz) {
										pow_timer = 1800;
										t1.Lazer();
									}
									itrr.remove();
									score=score+5;
								}
								break;
							}
							//other shots
							else {
								if(currRock.radius==30) {
									addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
									addAfter.add(new rock().setRadius(20).setSpawn(currRock.getX(), currRock.getY()));
								}
								if(currRock.radius==20) {
									addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
									addAfter.add(new rock().setRadius(10).setSpawn(currRock.getX(), currRock.getY()));
								}
								if(currRock.pow_health) {
									if(lifeTotal<10) {
										lifeTotal+=1;
									}
								}
								if(currRock.pow_shot) {
									pow_timer = 1800;
									t1.Shotgun();
								}
								if(currRock.pow_frz) {
									pow_timer = 1800;
									t1.Freeze();
								}
								if(currRock.pow_laz) {
									pow_timer = 1800;
									t1.Lazer();
								}
								itrr.remove();
								itrb.remove();
								score=score+5;
								break;
							}
						}
					}
				}
				
				
				g2d.setColor(Color.red);
				g2d.drawString("Power Timer: ", 10, 45);
				
				if(pow_timer>0) {
					g2d.setColor(Color.GREEN);
					g2d.drawString(String.valueOf(pow_timer), 86, 45);
					pow_timer-=1;
				}
				else {
					g2d.drawString("N/A", 86, 45);
					t1.noPower();
				}
				//adds new rocks
				for(int i = 0 ; i<addAfter.size();i++) {
					rocks.add(addAfter.get(i));
				}
				
				//display power timer
				g2d.setColor(Color.red);
				g2d.drawString("Current Power: ", 10, 60);
				if(t1.pow_shot) {
					g2d.setColor(Color.green);
					g2d.drawString("Shotgun", 95, 60);
				}
				else if(t1.pow_laz) {
					g2d.setColor(Color.green);
					g2d.drawString("Lazer", 95, 60);
				}
				else if(t1.pow_frz) {
					g2d.setColor(Color.green);
					g2d.drawString("Freeze", 95, 60);
				}
				else {
					g2d.setColor(Color.green);
					g2d.drawString("Blaster", 95, 60);
				}
				
				//display score
				g2d.setColor(Color.red);
				g2d.drawString("Score: ", 10, 75);
				g2d.setColor(Color.green);
				g2d.drawString(String.valueOf(score), 48, 75);
				
				
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
			//if(pow_shot) {
			//	t1.Shotgun();
			//}
			//else {
			//	t1.noPower();
			//}
			
			if(bullethold) {
				if(bulletspawntimer<1) {
					//shotgun powerup, currently toggable
					if(t1.pow_shot) {
						int numberOfBullets = 8;
						for(int i=0; i<numberOfBullets; i++) {
							bullet pellet = new bullet(t1).setRadius(3).Shotgun();
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
					else if(t1.pow_frz){
						bullet shot = new bullet(t1).setRadius(3).Freeze().setSpeed(0.50, 0.50);
						int decider = (int) Math.round(Math.random()*3+1);
						if(decider == 1) {
							shot.xVel = shot.xVel + ((Math.random()*0.75));
							shot.yVel = shot.yVel + ((Math.random()*0.75));
						}
						if(decider == 2) {
							shot.xVel = shot.xVel + ((Math.random()*0.75));
							shot.yVel = shot.yVel - ((Math.random()*0.75));
						}
						if(decider == 3) {
							shot.xVel = shot.xVel - ((Math.random()*0.75));
							shot.yVel = shot.yVel + ((Math.random()*0.75));
						}
						if(decider == 4) {
							shot.xVel = shot.xVel - ((Math.random()*0.75));
							shot.yVel = shot.yVel - ((Math.random()*0.75));
						}
						bullets.add(shot);
						
						bulletspawntimer = 8;
					}
					else {
						bullets.add(new bullet(t1));
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
				pow_shot = !pow_shot;
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
				pow_shot = false;
				pow_laz = false;
				pow_frz = false;
				
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
