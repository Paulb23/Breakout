package org.BrokenPixel.entity;

import org.BrokenPixel.graphics.Textures;

public abstract class Entity {

	private int width;
	private int height;
	private int xPosition;
	private int yPosition;
	private Textures texture;
	
	public Entity(final int width, final int height, final int xPosition, final int yPosition, String string) {
		this.width  = width;
		this.height = height;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.texture = new Textures(string, width, height);
	}
	
	public void blit() {
		this.texture.blit(xPosition, yPosition);
	}
	
	public void blit(int xoffset, int yoffset) {
		this.texture.blit(xPosition + xoffset, yPosition + yoffset);
	}
	
	public void setTexture(String string, int width, int height) {
		this.texture = new Textures(string, width, height);
	}
	public int getWidth    (){return this.width;}
	public int getHeight   (){return this.height;}
	public int getXPosition(){return this.xPosition;}
	public int getYPosition(){return this.yPosition;}
	
	public void setWidth    (final int width){this.width = width; this.texture.setWidth(width); }
	public void setHeight   (final int height){this.height = height; this.texture.setHeight(height);}
	public void setXPosition(final int xPosition){this.xPosition = xPosition;}
	public void setYPosition(final int yPosition){this.yPosition = yPosition;}

	
}
