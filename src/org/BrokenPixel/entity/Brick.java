package org.BrokenPixel.entity;

public class Brick extends Entity implements Drops {
	
	private int id;
	private int health;

	public Brick(int width, int height, int xPosition, int yPosition, String string, int health, final int id) {
		super(width, height, xPosition, yPosition, string);
		
		this.id = id;
		this.health = health;
	}
	
	public void Damge() {
		this.health -= 10;
	}
	
	public int getId() {return this.id; }
	public int getHealth() {return this.health; }

	@Override
	public void updatePosition() {
		this.setYPosition(this.getYPosition() + 2);
	}

	@Override
	public void onDestroy() {
		
	}
}
