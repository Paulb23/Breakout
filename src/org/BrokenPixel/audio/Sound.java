package org.BrokenPixel.audio;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {

	private Audio clip;
	
	public Sound(String path) {
		try {
			this.clip = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playMusic(Boolean loop) {
		this.clip.playAsMusic(1.0f, 0.3f, loop);
	}
	
	public void playEffcct(Boolean loop) {
		this.clip.playAsSoundEffect(1.0f, 1.0f, loop);
	}
	
}
