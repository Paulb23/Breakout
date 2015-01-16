package org.BrokenPixel.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Textures {
	
	private int textureWidth;
	private int textureHeight;
	private Texture texture;
	
	public Textures(String string, int width, int height) {
		this.textureWidth = width;
		this.textureHeight = height;
		this.texture = load(string);
	}
	
	private Texture load(final String string) {
		try {
			Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(string), GL11.GL_NEAREST);
			
			return texture;
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return null;
	}
	
	public void blit(final int x, final int y) {
		GL11.glLoadIdentity();
		this.texture.bind();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_QUADS);
		
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(x + this.textureWidth, y);
		
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2f(x + this.textureWidth , y + this.textureHeight);
			
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(x, y + this.textureHeight);
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public int getWidth()  { return this.textureWidth; }
	public int getHeight() { return this.textureHeight;}
	
	public void setWidth  (final int width)  	{  this.textureWidth  = width; }
	public void setHeight (final int height)	{  this.textureHeight = height;}
	public void setTexture(final String string) {  this.texture = load(string);}
}
