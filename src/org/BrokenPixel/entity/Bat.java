package org.BrokenPixel.entity;

import org.lwjgl.opengl.Display;

public class Bat extends Entity {

	private int speed;
	private int points;
	private int lives;
	
	public Bat(int width, int height, int xPosition, int yPosition, String string, int speed) {
		super(width, height, xPosition, yPosition, string);
		
		this.speed = speed;
		points = 0;
		lives = 4;
	}

	public void WrapSides() {
		if (this.getXPosition() < 0) {
			this.setXPosition(0);
		}
		
		if (this.getXPosition() + this.getWidth() > Display.getWidth()) {
			this.setXPosition(Display.getWidth() - this.getWidth());
		}
	}
	
	public void moveRight() {
		this.setXPosition(this.getXPosition() + speed);
	}
			
	public void moveLeft() {
		this.setXPosition(this.getXPosition() - speed);
	}
	
	public int getSpeed(){ return this.speed; }
	
	public void setSpeed(final int speed) { this.speed = speed; }

	public void addPoints(int points) { this.points += points;}
	public int getPoints() { return this.points; }
	
	public int getLives() { return this.lives; }
	public void setLives(int lives) { this.lives = lives; }
	public void takeLife() { this.lives -= 1; }
}
