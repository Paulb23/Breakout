package org.BrokenPixel.GUI;

import org.BrokenPixel.graphics.Textures;

public class Selector {

	private int x, y;
	private Textures texture;
	
	public Selector(String string, int x, int y, int width, int height){
		this.texture = new Textures(string, width, height);
		this.x = x;
		this.y = y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void blit() {
		this.texture.blit(this.x, this.y);
	}
	
}
