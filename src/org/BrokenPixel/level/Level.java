package org.BrokenPixel.level;

import java.io.File;

import org.BrokenPixel.entity.Ball;
import org.BrokenPixel.entity.Bat;

public class Level {
	
	private transient int currentLevel;
	private String[] levels;
	private int amountOfMaps;
	private Map map;
	
	public Level() {
		this.currentLevel = -1;
		this.map = new Map();
		
		int amountOfFiles = new File("res/maps").listFiles().length;
		File[] files = new File("res/maps").listFiles();		
		
		this.levels = new String[amountOfFiles];
		
		for (int i = 0; i < amountOfFiles; i ++) {
			if (files[i].getName().endsWith(".lvl")) {
				this.levels[i] = "res/maps/" + files[i].getName();
				amountOfMaps++;
			}
		}
	}
	
	public void loadNextLevel() {
		if (currentLevel + 1 < amountOfMaps) {
			this.currentLevel++;
		}
		
		this.map.loadMap(levels[currentLevel]);
	}
	
	public void LoadLevel(int level) {
		this.currentLevel = level;
		
		this.map.loadMap(levels[currentLevel]);
	}
	
	public void levelDraw(int xoffset, int yoffset) {
		this.map.drawMap(xoffset, yoffset);
	}
	
	public int levelCollision(Ball ball) {
		return this.map.mapCollision(ball);
	}
	
	public int UpdateDrops(Bat bat) {
		return this.map.UpdateDrops(bat);
	}

	public boolean anyBricks() {
		return this.map.anyBricks();
	}
	
	public int AmountOfMaps() {
		return this.amountOfMaps;
	}
	
	public int getCurrentLevel() { return this.currentLevel; }
}
