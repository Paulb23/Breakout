package org.BrokenPixel.particles;
import org.BrokenPixel.graphics.Textures;

public class Particles {

	private Textures texture;
	
	public Particles(final String path, final int w, final int h) {
		this.texture = new Textures(path, w, h);
	}
	
	public void blit(int x, int y) {
		this.texture.blit(x, y);
	}
	
}
