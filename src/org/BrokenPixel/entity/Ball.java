package org.BrokenPixel.entity;

import java.awt.Point;
import org.lwjgl.opengl.Display;

public class Ball extends Entity {

	private int speedY;
	private int speedX;
	private int Maxspeed;
	private Point[] trail;
	private boolean released;
	
	public Ball(int width, int height, int xPosition, int yPosition, String string, int speed) {
		super(width, height, xPosition, yPosition, string);
		
		this.Maxspeed = speed;
		this.speedX = 0;
		this.speedY = 0;
		
		this.released = false;
		
		trail = new Point[20];
		resetTrail();
	}
	
	public void updatePosition() {	
		addTrailPoint();
		
		this.setXPosition(this.getXPosition() - this.speedX);
		this.setYPosition(this.getYPosition() - this.speedY);
	}
	
	public void sideCollision(Bat player) {
		if(this.getXPosition() < 0 || this.getXPosition() + this.getWidth() > Display.getWidth()) {
			reverseXDirection();
		}
		
		if (this.getYPosition() < 0) {
			reverseYDirection();
		}
	}

	public void reverseXDirection() {
		this.speedX = -this.speedX;
	}
	
	public void reverseYDirection() {
		this.speedY = -this.speedY;
	}
	
	public void resetTrail() {
		for(int i = 0; i < this.trail.length; i++) {
			this.trail[i] = new Point(-100,-100);
		}
	}
	
	private void addTrailPoint() {
		boolean ajustPoints = false;
		
		for(int i = 0; i < this.trail.length; i ++) {
			if (trail[i].equals(new Point(-100,-100))){ 
				trail[i] = new Point(this.getXPosition(), this.getYPosition()); ajustPoints = false; break;
			} else {
				ajustPoints = true;
			}
		}
		
		if (ajustPoints) {
			for(int i= this.trail.length - 1; i > 0 ;i--){ 
			     this.trail[i] = this.trail[i-1];
			}
			
			this.trail[0] = new Point(this.getXPosition(), this.getYPosition()); 
		}
		
	}
	
	public int getSpeedX(){ return this.speedX; }
	public int getSpeedY(){ return this.speedY; }
	public int getMaxSpeed(){ return this.Maxspeed; }
	public boolean getReleased(){ return this.released; }
	public Point[] getTrail() {return this.trail;}
	
	public void setSpeedX(final int speed) { this.speedX = speed; }
	public void setSpeedY(final int speed) { this.speedY = speed; }
	public void setReleased(final boolean released) { this.released = released; }
	
}
