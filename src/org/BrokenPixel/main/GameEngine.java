package org.BrokenPixel.main;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.BrokenPixel.GUI.Button;
import org.BrokenPixel.GUI.Selector;
import org.BrokenPixel.audio.Sound;
import org.BrokenPixel.entity.Ball;
import org.BrokenPixel.entity.Bat;
import org.BrokenPixel.entity.Entity;
import org.BrokenPixel.graphics.Textures;
import org.BrokenPixel.input.Keyboards;
import org.BrokenPixel.level.Level;
import org.BrokenPixel.particles.Particles;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

public class GameEngine {

	private transient boolean running;
	
	private transient int SCREEN_WIDTH;
	private transient int SCREEN_HEIGHT;
	private transient String SCREEN_TITLE;
	private static transient final double FRAMERATE = 60;
	
	public Keyboards keybord;
	
	private transient double screenShakeTicks;
//	private transient boolean muted;
	
	public transient boolean tickUpdate;
	public transient int upTime;
	public transient double frames;
	public transient double ticks;
	
	private Bat player;;
	private Level level;
	
	private int ballTrail;
	
	private Textures background;
	private Textures menuTitle;
	private Textures pause;
	private Textures gameOver;
	private Textures gameWin;
	private Textures lives4;
	private Textures lives3;
	private Textures lives2;
	private Textures lives1;
	
	
	private ArrayList<Particles> particles = new ArrayList<Particles>();
	private ArrayList<Ball>      balls     = new ArrayList<Ball>();
	
	private ArrayList<Button>    buttons     = new ArrayList<Button>();
	private Selector selector;
	private Selector lvlselector;
	private int selectedOffset;
	
	private int currentPage;
	private int maxPage;
	
	private transient boolean showLives;
	private transient int loops;
	
	private Sound music;
	private Sound hit;
	private Sound collected;

	
	private GameStates gameState = GameStates.MENU;
	

	private enum GameStates {
		MENU,
		LEVELSELECT,
		GAME,
		GAMEOVER,
		GAMEWIN,
		PAUSE
	}
	
	
	/**
	 * Created a screen and title, set up the application
	 * @param screenWidth screen width
	 * @param screenHeight screen height
	 * @param title title of the screen
	 * @throws IOException 
	 */
	public GameEngine(final int screenWidth, final int screenHeight, final String title) throws IOException {
	
		try {
			Display.setDisplayMode(new DisplayMode(screenWidth,screenHeight));
			Display.setTitle(title);
			Display.create();

			Display.setIcon(new ByteBuffer[] {
			        loadIcon("res/icons/icon16.png"),
			        loadIcon("res/icons/icon32.png"),
			        loadIcon("res/icons/icon64.png"),
			        loadIcon("res/icons/icon128.png")
			    });
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Set Up OPENGL CODE	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                                          
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);   
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glMatrixMode(GL11.GL_PROJECTION);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glLoadIdentity();
		glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		
		this.SCREEN_WIDTH = screenWidth;
		this.SCREEN_HEIGHT = screenHeight;
		this.SCREEN_TITLE = title;
		
		this.running = false;
		//this.muted = false;
		this.screenShakeTicks = 0;
		this.keybord = new Keyboards();
		
		this.level = new Level();
		
		this.player = new Bat(64, 16, screenWidth / 2 - 64, screenHeight - 32, "res/textures/bat.png", 15);
		this.balls.add(new Ball(16, 16, screenWidth / 2 - 64, screenHeight - 32, "res/textures/ball.png", 5));
		
		this.ballTrail = AddParticle(new Particles("res/textures/particle.png", 8, 8));
		
		this.level = new Level();
		this.currentPage = 0;
		
		this.music = new Sound("res/audio/soundtrack.wav");
		this.hit = new Sound("res/audio/bounce.wav");
		this.collected = new Sound("res/audio/collect.wav");
		this.music.playMusic(true);
		
		background = new Textures("res/textures/background.png", 1920, 1180);
		menuTitle = new Textures("res/textures/UI/title.png", 400, 800);
		pause = new Textures("res/textures/UI/pause.png", 400, 800);
		this.gameOver = new Textures("res/textures/UI/gameOver.png", 1000, 200);
		this.gameWin = new Textures("res/textures/UI/gameWin.png", 1000, 200);
		this.lives4 = new Textures("res/textures/UI/lives4.png", 1000, 200);
		this.lives3 = new Textures("res/textures/UI/lives3.png", 1000, 200);
		this.lives2 = new Textures("res/textures/UI/lives2.png", 1000, 200);
		this.lives1 = new Textures("res/textures/UI/lives1.png", 1000, 200);
		lvlselector = new Selector("res/textures/UI/lvlselector.png", 0, 0, 100, 100);
		
	    showLives = true;
	    loops = 0;
		
		loadMenuButtons();
		
		
		start();
	}
	
	
	
	 private ByteBuffer loadIcon(String url) throws IOException {
	        InputStream is = new FileInputStream(url);
	        try {
	            PNGDecoder decoder = new PNGDecoder(is);
	            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
	            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
	            bb.flip();
	            return bb;
	        } finally {
	            is.close();
	        }
	    }
	
	/**
	 *  main game loop
	 */
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0D / FRAMERATE;
		long timer = System.currentTimeMillis();
	
		double delta = 0;
		double fps = 0;
		double tick = 0;
		upTime = 0; level.loadNextLevel(); showLives = true;
		while(running) {
			final long now  = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
				while (delta >= 1) {
					ticks();
					tickUpdate = true;
					if (screenShakeTicks > 0) {
						screenShakeTicks -= delta;
					}
					tick++;
					delta--;
				}
				if (showLives && loops >= 2) {					
					 loops = 0;
					 showLives = false;
				}
			 render();
			 fps++;
			 tickUpdate = false;
			 
		    if (System.currentTimeMillis() - timer > 1000) {
		    	frames = fps;
				ticks = tick;	
		    	
			  Display.setTitle(SCREEN_TITLE +" | Frames: " + fps + " | ticks: " + tick);
			  timer += 1000;
			  fps = 0;
			  tick = 0;
			  if (showLives) {
				  loops += 1;
			  }
			  
			  upTime += 1;
			}
			
		}
		
		stop();
	}
	
	
	
	/**
	 *  Game logic
	 */
	private void ticks() {
		switch (this.gameState) {
			case MENU:
				menuTicks(); 
				break;
			case LEVELSELECT:
				levelSelectTicks();
				break;
			case GAME:
				gameTicks();
				break;
			case PAUSE:
				pauseTicks();
				break;
			case GAMEOVER:
				GameoverTicks();
				break;
			case GAMEWIN:
				GameWinTicks();
				break;
			default:
				break;
		}	
		// close when requested
		if (Display.isCloseRequested()) {
			running = false;
		}
	

	}
	
	
	/**
	 *  Main Menu logic
	 */
	private void menuTicks() {
		
		this.selector.setXY(buttons.get(selectedOffset).getX() + 10, buttons.get(selectedOffset).getY() + -20);
		
		while (Keyboard.next()){
			if (!Keyboard.getEventKeyState()) {  
				buttonContols();
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (buttons.get(selectedOffset).getName() == "play") {
						selectedOffset = 0;
						currentPage = 0;
						loadLevelSelectMenu();
						this.gameState = GameStates.LEVELSELECT;
					}
					
					if (buttons.get(selectedOffset).getName() == "exit") {
						running = false;
					}
				}
			}
		}
	}
	
	/**
	 *  Game over Ticks
	 */
	private void GameoverTicks() {
		this.selector.setXY(buttons.get(selectedOffset).getX() + 10, buttons.get(selectedOffset).getY() + -20);
		
		while (Keyboard.next()){
			if (!Keyboard.getEventKeyState()) {  
				buttonContols();
				
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && selectedOffset != 0) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset--;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && selectedOffset < buttons.size() - 1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset++;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (buttons.get(selectedOffset).getName() == "menu") {
						loadMenuButtons();
						this.gameState = GameStates.MENU;
					}
					
					if (buttons.get(selectedOffset).getName() == "retry") {
						this.level.LoadLevel(this.level.getCurrentLevel());
						resetLevel();
						gameState = GameStates.GAME;
					}
				}
			}
		}
	}
	
	
	/**
	 *  Game over Ticks
	 */
	private void GameWinTicks() {
		this.selector.setXY(buttons.get(selectedOffset).getX() + 10, buttons.get(selectedOffset).getY() + -20);
		
		while (Keyboard.next()){
			if (!Keyboard.getEventKeyState()) {  
				buttonContols();
				
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && selectedOffset != 0) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset--;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && selectedOffset < buttons.size() - 1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset++;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (buttons.get(selectedOffset).getName() == "menu") {
						loadMenuButtons();
						this.gameState = GameStates.MENU;
					}
					
					if (buttons.get(selectedOffset).getName() == "nextlvl") {
						this.level.loadNextLevel();
						resetLevel();
						gameState = GameStates.GAME;
					}
				}
			}
		}
	}
	
	/**
	 * Pause ticks
	 */
	private void pauseTicks() {
		this.selector.setXY(buttons.get(selectedOffset).getX() + 10, buttons.get(selectedOffset).getY() + -20);
		
		
		while (Keyboard.next()){
			if (!Keyboard.getEventKeyState()) {  
				buttonContols();
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if (buttons.get(selectedOffset).getName() == "resume") {
						this.keybord.menu = false;
						this.gameState = GameStates.GAME;
					}
					
					if (buttons.get(selectedOffset).getName() == "menu") {
						loadMenuButtons();
						this.keybord.menu = false;
						this.gameState = GameStates.MENU;
					}
						
					if (buttons.get(selectedOffset).getName() == "exit") {
						running = false;
					}
				}
			}
		}
	}
	
	/**
	 *  Level select logic
	 */
	private void levelSelectTicks() {
		
		if (buttons.get(selectedOffset).getName() == "level") {
			this.lvlselector.setXY(buttons.get(selectedOffset).getX(), buttons.get(selectedOffset).getY());
		} else {
			this.selector.setXY(buttons.get(selectedOffset).getX(), buttons.get(selectedOffset).getY());
		}
		
		while (Keyboard.next()){
			if (!Keyboard.getEventKeyState()) {  

				
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {loadMenuButtons(); this.gameState = GameStates.MENU;}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					if ( this.buttons.get(selectedOffset).getName() == "level") {this.level.LoadLevel(selectedOffset); resetLevel(); this.gameState = GameStates.GAME;}
					if ( this.buttons.get(selectedOffset).getName() == "next" && currentPage < maxPage) {selectedOffset = 0; currentPage += 1; 	loadLevelSelectMenu();}
					if ( this.buttons.get(selectedOffset).getName() == "previous" && currentPage > 0) {selectedOffset = 0;currentPage -= 1; 	loadLevelSelectMenu();}
					if ( this.buttons.get(selectedOffset).getName() == "back") {loadMenuButtons(); this.gameState = GameStates.MENU;}
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset - 5 > -1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset-=5;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset - 4 > -1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset-=4;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset - 3 > -1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset-=3;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset - 2 > -1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset-=2;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset - 1 > -1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset-=1;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 6) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=5;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 5) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=4;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 4) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=3;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 3) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=2;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 2) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=1;
					buttons.get(selectedOffset).switchSelected();
				} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset+=1;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && selectedOffset != 0) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset--;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && selectedOffset < buttons.size() - 1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset++;
					buttons.get(selectedOffset).switchSelected();
				}
			}
		}
	}
	
	
	/**
	 *  Menu button controls
	 */
	private void buttonContols() {
				if (Keyboard.getEventKey() == Keyboard.KEY_UP && selectedOffset != 0) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset--;
					buttons.get(selectedOffset).switchSelected();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && selectedOffset < buttons.size() - 1) {
					buttons.get(selectedOffset).switchSelected();
					selectedOffset++;
					buttons.get(selectedOffset).switchSelected();
				}
	}
	
	private void gameTicks() {
		if (!showLives) {
			keybord.update();
			
			playerUpdate();
			ballsUpdate();
			
			if (this.keybord.menu || !Display.isActive()) {
				loadPauseButtons();
				this.gameState = GameStates.PAUSE;
			}
			
			// any lives?
			if (this.player.getLives() <= 0) {
				gameOverButtons();
				this.gameState = GameStates.GAMEOVER;
			}
			
			// are there any brick left?
			if (this.level.anyBricks()) {
				gameWinButtons();
				gameState = GameStates.GAMEWIN;
			}
		}
	}
	
	private void playerUpdate() {
		// update the player
		if (keybord.left) {
			this.player.moveLeft();
		}
		if (keybord.right) {
			this.player.moveRight();
		}
		this.player.WrapSides();
	}
	
	private void ballsUpdate() {
		for (int i = 0; i < balls.size(); i ++) {
			// update the ball
			this.balls.get(i).updatePosition();
			this.balls.get(i).sideCollision(this.player);
			
			// ball moment while it is attached to the bat, and shoot code
			if (!this.balls.get(i).getReleased() && keybord.shoot) {
				this.balls.get(i).setSpeedY(-this.balls.get(i).getMaxSpeed());
				this.balls.get(i).setReleased(true);
			} else if (!this.balls.get(i).getReleased()) {
				if (balls.size() == 1) {
					this.balls.get(i).setXPosition(this.player.getXPosition() + this.player.getWidth() / 3);
					this.balls.get(i).setYPosition(this.player.getYPosition() - this.player.getHeight());
					balls.get(i).setWidth(16);
					balls.get(i).setHeight(16);
				} else {
					this.balls.remove(i);
					break;
				}
			}
			
			// ball and brick collision 
			int collision = this.level.levelCollision(this.balls.get(i));
			int IdOfBrick = this.level.UpdateDrops(this.player);
			if (collision == 1) {
				screenShake(5);
				hit.playEffcct(false);
			}
			if (IdOfBrick != -1) {
				collected.playEffcct(false);
			}
			
			// Special bricks affect handling
			switch (IdOfBrick) {
				case 2:
					break;
				case 3:
					break;
				case 5:
					for (int q = 1; q < 4; q++) {
						this.balls.add(new Ball(16, 16, this.player.getXPosition(), this.player.getYPosition(), "res/textures/ball.png", 5));
						Random random = new Random();
						
						boolean positiveX =  random.nextBoolean();
						
						if (positiveX) {
							this.balls.get(i + q).setSpeedX(5);
						} else {
							this.balls.get(i + q).setSpeedX(-5);
						}
						
						this.balls.get(i +q).setSpeedY(-5);
						
						this.balls.get(i + q).setReleased(true);
					}
					break;
				case 6:
					for (Ball b : balls) {
						b.setSpeedX(b.getMaxSpeed() + 3);
						b.setSpeedY(b.getMaxSpeed() + 3);
					}
					break;
				case 7:
					for (Ball b : balls) {
						b.setSpeedX(b.getMaxSpeed() - 3);
						b.setSpeedY(b.getMaxSpeed() - 3);
					}
					break;
				case 8:
					if (player.getWidth() < 84) {
						this.player.setWidth(this.player.getWidth() + 20);
					}
					break;
				case 9:
					if (player.getWidth() > 44) {
						this.player.setWidth(this.player.getWidth() - 20);
					}
					break;
				case 10:
					player.addPoints(10);
					break;
				case 11:
					player.addPoints(20);
					break;
				case 12:
					for (Ball b : balls) {
						b.setWidth(b.getWidth() / 2);
						b.setHeight(b.getHeight() / 2);
					}
					break;
				case 13:
					for (Ball b : balls) {
						b.setWidth(b.getWidth() * 2);
						b.setHeight(b.getHeight() * 2);
					}
					break;
			}
				
			// bat and ball collision
			if (collision(this.player, this.balls.get(i))) {
				if (this.balls.get(i).getXPosition() < this.player.getXPosition() + this.player.getWidth() / 4) {
					this.balls.get(i).setSpeedX(this.balls.get(i).getMaxSpeed());
				}
				if (this.balls.get(i).getXPosition() > this.player.getXPosition() + this.player.getWidth() / 2) {
					this.balls.get(i).setSpeedX(-this.balls.get(i).getMaxSpeed());
				}
				this.balls.get(i).reverseYDirection();
				screenShake(5);
				hit.playEffcct(false);
			}
			
			
			if (this.balls.get(i).getYPosition() > Display.getWidth() - 200) {
				this.balls.get(i).setReleased(false);
				
				if (this.balls.size() == 1){player.takeLife();
					this.balls.get(i).setXPosition(this.player.getXPosition() + this.player.getWidth() / 3);
					this.balls.get(i).setYPosition(this.player.getYPosition() - this.player.getHeight());
					this.balls.get(i).setSpeedX(0);
					this.balls.get(i).setSpeedY(0);
					this.balls.get(i).setReleased(false);
					this.balls.get(i).resetTrail();
					showLives = true;
				} else {
					this.balls.remove(i);
				}
			}
		}
	}
	
	
	/**
	 *  Rendering code
	 */
	private void render() {
		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		 
		 int x = 0;
		 int y = 0;
		 
		 // Screen shake
         if(screenShakeTicks > 0){
        	Random rnd = new Random();
        	x = rnd.nextInt(5);
        	y = rnd.nextInt(5);
        }
		
         // Draw the background
         background.blit(0, 0);
         
         
         switch (this.gameState) {
         	case MENU:
         		menuRender();
         		break;
         	case LEVELSELECT:
         		levelSelectRender();
         		break;
         	case GAME:
         		gameRender(x, y);
         		break;
         	case PAUSE:
         		gameRender(x, y);
         		pauseRender();
         		break;
         	case GAMEOVER:
         		GameoverRender();
         		break;
			case GAMEWIN:
				GamewinRender();
				break;
		default:
			break;
         }
         // Draw the player and the ball
        
       
         
 		// update the screen
		Display.update();
	}
	
	/**
	 * draws the main menu
	 */
	private void menuRender(){
		menuTitle.blit(250, 0);
		
		for (Button i : buttons) {
			i.blit();
		}
		
		selector.blit();
		
	}
	
	
	/**
	 * Pause render
	 */
	private void pauseRender() {
		pause.blit(250, 0);
		
		for (Button i : buttons) {
			i.blit();
		}
		
		selector.blit();
	}
	
	private void GameoverRender() {
		this.level.levelDraw(0, 0);
		
		this.gameOver.blit(0, 200);
		
		for (Button i : buttons) {
			i.blit();
		}
		
		selector.blit();
	}
	
	private void GamewinRender() {
		this.level.levelDraw(0, 0);
		
		this.gameWin.blit(0, 200);
		
		for (Button i : buttons) {
			i.blit();
		}
		
		selector.blit();
	}
	
	/**
	 * draws the level select
	 */
	private void levelSelectRender() {	
		for (Button i : buttons) {
			i.blit();
		}
		
		if (buttons.get(selectedOffset).getName() == "level") {
			lvlselector.blit();
		} else {
			selector.blit();
		}
	}
	
	private void gameRender(int xoffset, int yoffset) {
		for (int i = 0; i < balls.size(); i++) {  
	         this.balls.get(i).blit(xoffset, yoffset);
	         this.player.blit(xoffset, yoffset);
	         
	         // if the ball is released draw the trail
	         if (this.balls.get(i).getReleased()) {
		         Point[] trail = this.balls.get(i).getTrail();
		         for (int x = 0; x < trail.length; x++) {
		        	 particles.get(ballTrail).blit((int) trail[x].getX(), (int) trail[x].getY());
		         }
	         }
	       } 
	        
		// Draw the bricks
		this.level.levelDraw(xoffset, yoffset);
		
		if (showLives) {
			switch (player.getLives()) {
				case 4:
					lives4.blit(0, 200);
					break;
				case 3:
					lives3.blit(0, 200);
					break;
				case 2:
					lives2.blit(0, 200);
					break;
				case 1:
					lives1.blit(0, 200);
					break;
			}
		}
	}
	
	
	
	/**
	 *  adds a particle effect to the array list 
	 * @param entity particle effect
	 * @return offset of the effect
	 */
	public int AddParticle(Particles entity) {
		particles.add(entity);
		return particles.size() - 1;
	}
	
	
	
	/**
	 *  set the screen shake length  
	 * @param milliseconds how long to shake the screen for
	 */
	public void screenShake(double milliseconds) {
		screenShakeTicks = milliseconds;
	}
	
	
	
	/**
	 * return the screen width
	 * @return screen width
	 */
	public int getScreenWidth() {
		return this.SCREEN_WIDTH;
	}
	
	
	
	/**
	 *  returns screen height
	 * @return screen height
	 */
	public int getScreenHeight() {
		return this.SCREEN_HEIGHT;
	}
	
	
	
	/**
	 *  delays the program
	 */
	public void pause() {
		try {
			TimeUnit.MILLISECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  loads the main menu button
	 */
	private void loadMenuButtons() {
		
		// loads the selector
		selectedOffset = 0;
		this.selector = new Selector("res/textures/UI/mainmenuselector.png", 250, 100, 100, 100);
		
		// clear the old buttons
		buttons.clear();
		buttons.add(new Button("res/textures/UI/play.png", 250, 100, 400, 50, "play"));
		buttons.add(new Button("res/textures/UI/exit.png", 250, 400, 400, 50, "exit"));
		
		// set 0 to be selected
		buttons.get(selectedOffset).switchSelected();
	}
	
	/**
	 *  Loads the buttons for the level select
	 */
	private void loadLevelSelectMenu() {
		// load the selector
		this.selector = new Selector("res/textures/UI/lvlbselector.png", 250, 100, 400, 50);
		
		// get the amount of maps and set the x y position
		int AmountOfmaps = this.level.AmountOfMaps();
		int x = 50;
		int y = 20;
		
		// work out the max page
		this.maxPage = (int ) AmountOfmaps / 15;
		
		// clear the old buttons
		buttons.clear();
		
		// loop through the and add the buttons
		for (int i = (currentPage * 15); i < (currentPage * 15) + 15; i++) {
			File f = new File("res/icons/maps/map" + (i+1) + ".png");  
			
			// if the icon exits use it else load the N/A icon
			if (f.exists()) {
				buttons.add(new Button("res/icons/maps/map" + (i+1) + ".png", x, y, 100, 100, "level"));
			} else {
				buttons.add(new Button("res/icons/maps/na.png", x, y, 100, 100, "level"));
			}
			
			// increase the x value
			x += 150;

			// if we need a new row ajust the x and y coords
			if (x + 150 > this.getScreenWidth()) {
				x = 50;
				y += 150;
			}
			
			// if we have reached the maximum amout of maps break out of the loop
			if (i + 2 > AmountOfmaps) {
				break;
			}
		
		}
		
		// set button 0 to be selected
		buttons.get(0).switchSelected();
		
		// add the menu button
		buttons.add(new Button("res/textures/UI/back.png", 260, SCREEN_HEIGHT - 140, 400, 50, "back"));
		
		// hide the previous and next buttons if we can't use them
		if (currentPage != 0) {
			buttons.add(new Button("res/textures/UI/previous.png", 10, SCREEN_HEIGHT - 70, 400, 50, "previous"));
		}
		
		if (currentPage != maxPage) {
			buttons.add(new Button("res/textures/UI/next.png", SCREEN_WIDTH - 325, SCREEN_HEIGHT - 70, 400, 50, "next"));
		}
	}
	
	
	/**
	 *  Load pause Buttons
	 */
	private void loadPauseButtons() {
		selectedOffset = 0;
		this.selector = new Selector("res/textures/UI/mainmenuselector.png", 250, 100, 100, 100);
		
		// clear the old buttons
		buttons.clear();
		buttons.add(new Button("res/textures/UI/resume.png", 250, 100, 400, 50, "resume"));
		buttons.add(new Button("res/textures/UI/back.png", 250, 250, 400, 50, "menu"));
		buttons.add(new Button("res/textures/UI/exit.png", 250, 400, 400, 50, "exit"));
		
		// set 0 to be selected
		buttons.get(selectedOffset).switchSelected();
	}
	
	
	/**
	 *  Checks collision between two entity
	 * 
	 * @param one Entity one
	 * @param two Entity two
	 * @return true on collision, else false
	 */
	public boolean collision(final Entity one, final Entity two) {
		return ((one.getYPosition() + one.getHeight()) <= two.getYPosition() || one.getYPosition() >= (two.getYPosition() + two.getHeight()) || (one.getXPosition() + one.getWidth()) <= two.getXPosition() || one.getXPosition() >= (two.getXPosition() + two.getHeight()) ) ? false : true;
	}
	
	
	
	/**
	 *  Restart the level
	 */
	private void resetLevel() {
		balls.clear();
		
		//reset the ball and trail
		this.balls.add(new Ball(16, 16, SCREEN_WIDTH / 2 - 64, SCREEN_HEIGHT - 32, "res/textures/ball.png", 5));
		
		// reset the player
		this.player.setWidth(64);
		this.player.setXPosition((SCREEN_WIDTH / 2) - this.balls.get(0).getWidth());
		this.player.setYPosition(SCREEN_HEIGHT - this.balls.get(0).getHeight() * 2);
		this.player.setLives(4);
		
		// add the game over buttons
		this.buttons.clear();
		selectedOffset = 0;
		this.selector = new Selector("res/textures/UI/mainmenuselector.png", 250, 100, 100, 100);
		
		buttons.add(new Button("res/textures/UI/back.png", 150, 300, 400, 50, "menu"));
		
		buttons.get(selectedOffset).switchSelected();
		showLives = true;
	}
	
	
	private void gameOverButtons() {
		buttons.add(new Button("res/textures/UI/retry.png", 450, 300, 400, 50, "retry"));
	}

	private void gameWinButtons() {
		buttons.add(new Button("res/textures/UI/nextlvl.png", 450, 300, 400, 50, "nextlvl"));
	}
	
	/**
	 *  Starts the Application
	 */
	private void start() {
		running = true;
		run();
	}
	
	
	
	/**
	 *  Stops the Application
	 */
	private void stop() {
		Display.destroy();
		
		System.exit(0);
	}

}
