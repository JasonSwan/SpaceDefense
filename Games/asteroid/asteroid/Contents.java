package asteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.io.File;
import java.io.FileReader;
//file-path imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contents extends JPanel implements KeyListener, ActionListener {
	
	private Timer timer;
	private int delay = 8;
	
	Wall w1 = new Wall();
	spacebase b1 = new spacebase();
	Turret t1 = new Turret();
	
	boolean bullethold;
	
	ArrayList<rock> rocks = new ArrayList<rock>();
	ArrayList<bullet> bullets = new ArrayList<bullet>();
	//rock-spawn timer
	int spawntimer = 1000;
	//bullet spawn timer
	int bulletspawntimer = 0;
	//power-up timer
	int pow_timer;
	//lazer add-after bullet timer
	//and add-after bullet coordinates and velocity
	int lazer_timer;
	double lazer_angleX; double lazer_angleY;
	double lazer_x; double lazer_y;
	
	
	//GAME SCREENS
	//menu
	boolean gameMenu;
	//start
	boolean gameStart;
	//game over
	boolean gameOver;
	
	boolean scoreOnBoard;
	boolean scoreFound;
	int placement;
	
	//player life
	int lifeTotal;
	//player score
	int score;
	List<String> scoreLines;
	String playerName;
	
	
	
	public Contents() {
		rocks.add(new rock());
		super.setDoubleBuffered(true);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
		bullethold = false;
		
		gameMenu = true;
		gameStart = false;
		gameOver = false;
		scoreOnBoard=false;
		scoreFound=false;
		//restart = false;
		lifeTotal = 10;
		score = 0;
		
		playerName = "";
		
		//inital reading of highscore file
		try {
			File scoreFile = new File("asteroid/scores.txt");
			scoreFile.createNewFile();
			String filename="asteroid/scores.txt";
		    Path pathToFile = Paths.get(filename);
		    //System.out.println(pathToFile.toAbsolutePath());
			//get list at start of game, modify at end of game
		    scoreLines = Files.readAllLines( pathToFile);
		    
		    //fills the file with empty scores
		    Writer file2Add = new BufferedWriter(new FileWriter(filename, true));
		    if(scoreLines.size()<9) {
			    for(int i = 0; i < 9-scoreLines.size(); i++) {
			    	if(i == 0) {
			    		file2Add.append("null , 0");
			    	}
			    	file2Add.append(System.lineSeparator()+"null , 0");
			    }
		    }
		    file2Add.close();
		    scoreLines = Files.readAllLines(pathToFile);
		    /*
		    //prints lines in terminal
			for(int i = 0; i <scoreLines.size();i++) {
				System.out.println(scoreLines.get(i));
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//This is where objects are drawn to the JFRAME
	@Override
	public void paintComponent(Graphics g) {
		//background
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, 1280, 720);
		
		//MENU SCREEN
		if(gameMenu){
			g2d.setColor(Color.white);
			g2d.drawString("PRESS 'Enter' TO START", 570, 250);
			t1.draw(g2d);
			w1.draw(g2d);
			b1.draw(g2d);
		}
		//GAME OVER SCREEN
		else if(gameOver) {
			g2d.setColor(Color.white);
			g2d.drawString("GAME OVER", 620, 345);
			g2d.setColor(Color.white);
			g2d.drawString("SCORE: " + String.valueOf(score), 622, 360);
			g2d.drawString("PRESS 'ENTER' TO RESTART", 570, 375);
			//check if player score beats anything on the highscore list
			if(scoreFound==false) {
				for(int i = 0; i<scoreLines.size(); i++) {
						Pattern pattern = Pattern.compile("\\ , (\\d+)", Pattern.CASE_INSENSITIVE);
					    Matcher matcher = pattern.matcher(scoreLines.get(i));
					    matcher.find();
						if(score>Integer.parseInt(matcher.group(1))) {
							scoreOnBoard = true;
							playerName = "";
							placement = i;
							scoreFound = true;
							break;
						}
						
				}
			}
			//display the highscores
			if(scoreOnBoard) {
				g2d.drawString(playerName, 10, 15+(placement*15));
				g2d.drawString("<- Type Name Here (10 Char max)",100, 15+(placement*15));
				g2d.drawString("Press 'Enter' to submit", 100, 30+(placement*15));
			}
			else {
				for(int i = 0; i<scoreLines.size(); i++) {
					g2d.drawString(scoreLines.get(i), 10, i*15+15);
				}
			}
			
			//timer.stop();
		}
		//GAME START SCREEN
		else if(gameStart) {
			
			//game pieces
			t1.draw(g2d);
			w1.draw(g2d);
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
				//0 life is game over
				if(lifeTotal < 1) {
					gameOver = true;
				}
				
				//hiddenCalc shows the rocks relation to the wall if both were rotated to 0 degrees, easier for calculations
				for(int i = 0; i<rocks.size();i++) {
					rocks.get(i).draw(g2d);
					rocks.get(i).hiddenCalc(g2d,w1);
					rocks.get(i).wallCollision(w1);
				}
				
				
				//draw bullets and set time for next bullet
				//draw text to show cooldown of next shot
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
				//add rocks to empty board so that players get no down time
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
				//removes bullets once they are off teh screen
				Iterator<bullet> itrb = bullets.iterator();
				while(itrb.hasNext()) {
					bullet currBull = itrb.next();
					if(currBull.deadbul) {
						itrb.remove();
					}
				}
				//rock-bullet collision iterator
				//takes each bullet and checks if they are colliding with a rock
				ArrayList <rock> addAfter = new ArrayList<rock>();
				itrb = bullets.iterator();
				while(itrb.hasNext()) {
					itrr = rocks.iterator();
					bullet currBull = itrb.next();
					while(itrr.hasNext()) {
						rock currRock = itrr.next();
						if( currRock.bulletCollision(currBull)) {
							//rules of collision if freeze shot
							//rock will gradually slow each hit until it reaches a min speed in which it explodes
							if(currBull.pow_frz) {
								currRock.setSpeed(0.80, 0.80);
								itrb.remove();
								if(Math.abs(currRock.xVel) < 0.08 && Math.abs(currRock.yVel) < 0.08) {
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
							//rules of collision for other shots
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
								if(!currBull.pow_laz) {
									itrb.remove();
								}
								score=score+5;
								break;
							}
						}
					}
				}

				//adds new rocks
				for(int i = 0 ; i<addAfter.size();i++) {
					rocks.add(addAfter.get(i));
				}
				
				//displays power-up timer
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
				
				//display power-up type
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
			//add-after bullets for lazer
			if(lazer_timer>0) {
				bullets.add(new bullet(t1).Lazer().setSpeed(1.3, 1.3).setVelocity(lazer_angleX, lazer_angleY).setSpawn(lazer_x, lazer_y));
				lazer_timer-=1;
			}
			//if spacebar is pressed or held down
			if(bullethold) {
				if(bulletspawntimer<1) {
					//shotgun powerup
					if(t1.pow_shot) {
						int numberOfBullets = 8;
						//generates random speed modifiers for each pellet of the shotgun shot
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
						//generates freeze shot with speed modifiers
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
						
						bulletspawntimer = 5;
					}
					//lazer shot is fired, and coordinates are recorded for lazer bullets in next frame
					else if(t1.pow_laz) {
						lazer_timer=10;
						bullet lazer = new bullet(t1).Lazer().setSpeed(1.3, 1.3);
						bullets.add(lazer);
						lazer_angleX=lazer.xVel;
						lazer_angleY=lazer.yVel;
						lazer_x=lazer.x;
						lazer_y=lazer.y;
						bulletspawntimer = 150;
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
		//only used when current player gets on the highscore board
		if(scoreOnBoard) {
			if(Character.isLetter(e.getKeyChar()) && playerName.length()<10) {
				playerName+=e.getKeyChar();
			}
		}
		
		
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
				//pow_shot = !pow_shot;
			}
		}
		
		//START GAME
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameMenu) {
				gameStart = true;
				gameMenu=false;
			}
			//if player score is on the highscore
			if(scoreOnBoard) {
				//update scorefile here
				scoreOnBoard=false;
				try {
					String filename="asteroid/scores.txt";
				    Path pathToFile = Paths.get(filename);
				    scoreLines = Files.readAllLines( pathToFile);
				    
				    BufferedReader reader = new BufferedReader(new FileReader(filename));
				    
				    
				    String line = reader.readLine();
				    String oldFile = "";
				    int replace=0;
				    boolean moveDown = false;
				    String hold = "";
				    while(line!=null) {
				    	if(replace==placement) {;
				    		oldFile=oldFile+(playerName+" , "+score)+System.lineSeparator();
				    		hold = line;
				    		line = reader.readLine();
				    		replace+=1;
				    		moveDown = true;
				    		continue;
				    	}
				    	if(moveDown) {
				    		oldFile = oldFile+hold+System.lineSeparator();
				    		hold = line;
				    		line = reader.readLine();
					    	replace+=1;
					    	continue;
				    	}
				    	oldFile = oldFile+line+System.lineSeparator();
				    	hold = line;
				    	line = reader.readLine();
				    	replace+=1;
				    }
				    //String newContent = oldFile.replace(scoreLines.get(placement), playerName+" , "+score);
				    FileWriter writer = new FileWriter(filename);
				    writer.write(oldFile);
				    reader.close();
				    writer.close();
				    scoreLines = Files.readAllLines(pathToFile);
				}
				catch(IOException e1){
					e1.printStackTrace();
				}
				
				
			}
			else if(gameOver) {
				score = 0;
				lifeTotal = 10;
				w1.restart();
				t1.restart();

				rocks.removeAll(rocks);
				bullets.removeAll(bullets);
				rocks.add(new rock());
				
				spawntimer = 1000;
				t1.noPower();
				//pow_shot = false;
				//pow_laz = false;
				//pow_frz = false;
				
				gameMenu=true;
				gameOver = false;
				gameStart = false;
				
				scoreOnBoard = false;
				scoreFound = false;
				placement = 0;
				
				playerName="";
				
				//update highscore file here
			}
			
		}
		if(scoreOnBoard) {
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if(playerName.length()>0) {
					playerName = playerName.substring(0, playerName.length()-1);
				}
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
