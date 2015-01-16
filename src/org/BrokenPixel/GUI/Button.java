package org.BrokenPixel.GUI;

import org.BrokenPixel.graphics.Textures;

public class Button {
	
	private Textures texture;
	private int x,y;
	private boolean selected;
	private String name;
	
	public Button(String string, int x, int y, int width, int height, String name) {
		
		this.texture = new Textures(string, width, height);
		this.x = x;
		this.y = y;
		this.name = name;
		
		this.selected = false;
		
	}
	
	public void blit() {
		this.texture.blit(this.x, this.y);
	}
	
	public void switchSelected() {
		this.selected = (selected) ? false : true;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public String  getName() { return this.name; }
}
