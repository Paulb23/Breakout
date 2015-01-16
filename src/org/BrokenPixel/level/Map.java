package org.BrokenPixel.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import org.BrokenPixel.entity.Ball;
import org.BrokenPixel.entity.Bat;
import org.BrokenPixel.entity.Brick;
import org.BrokenPixel.entity.Entity;

public class Map {
	
	private LinkedList<Brick> bricks;
	private LinkedList<Brick> drops;
	private int amountOfIndestrucableBricks;
	
	public Map() {
		bricks = new LinkedList<Brick>();
		drops = new LinkedList<Brick>();
	}
	
	public void loadMap(String path) {
		bricks.clear();
		drops.clear();
		amountOfIndestrucableBricks = 0;
		
		File file = new File(path);
		try {
			Scanner scanner = new Scanner(file);
	
			for (int x = 1; x < 12; x ++) {
				for (int y = 1; y < 13; y ++) {
					switch (scanner.nextInt()) {
						case 1:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/normal.png", 10, 1));
							break;
						case 2:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/normalx2.png", 20, 2));
							break;
						case 3:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/normalx3.png", 30, 3));
							break;
						case 4:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/indestructble.png",10, 4));
							amountOfIndestrucableBricks++;
							break;
						case 5:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/moreBalls.png", 10, 5));
							break;
						case 6:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/fast.png", 10, 6));
							break;
						case 7:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/slow.png", 10, 7));
							break;
						case 8:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/expand.png", 10, 8));
							break;
						case 9:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/shrink.png", 10, 9));
							break;
						case 10:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/10points.png", 10, 10));
							break; 
						case 11:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/20points.png", 10, 11));
							break;
						case 12:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/ballshrink.png", 10, 12));
							break;
						case 13:
							bricks.add(new Brick(64, 32, x *64 - 10, y * 32, "res/textures/bricks/ballgrow.png", 10, 13));
							break;
					}
				}
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void drawMap(int xoffset, int yoffset) {
		for (int i = 0; i  < bricks.size(); i++) {
				bricks.get(i).blit(xoffset, yoffset);
		}
		
		for (int i = 0; i  < drops.size(); i++) {
			drops.get(i).blit(xoffset, yoffset);
		}
		
	}
	
	public int mapCollision(Ball ball) {
		int collision = 0;
		
		for (int i = 0; i  < bricks.size(); i++) {
				if (collision(bricks.get(i), ball)) {
					if (ball.getXPosition() < bricks.get(i).getXPosition() + bricks.get(i).getWidth() / 4) {
						ball.setSpeedX(ball.getMaxSpeed());
					}
					if (ball.getXPosition() > bricks.get(i).getXPosition() + bricks.get(i).getWidth() / 2) {
						ball.setSpeedX(-ball.getMaxSpeed());
					}
					ball.reverseYDirection();	
					
					int id = bricks.get(i).getId();
					
					bricks.get(i).Damge();
					
					if (id == 2 && bricks.get(i).getHealth() == 10) {
						bricks.get(i).setTexture("res/textures/bricks/normalx2x1.png", 64, 32);
					}
					
					if (id == 3) {
						if (bricks.get(i).getHealth() == 10) {
							bricks.get(i).setTexture("res/textures/bricks/normalx3x1.png", 64, 32);
						} else if (bricks.get(i).getHealth() == 20) {
							bricks.get(i).setTexture("res/textures/bricks/normalx3x2.png", 64, 32);
						}
					}
					
					if (bricks.get(i).getId() != 4 && bricks.get(i).getHealth() <= 0) {
						if (id > 4) {
							
							switch (id) {
								case 5:
									bricks.get(i).setTexture("res/textures/drops/dropballs.png", 16, 16);
									break;
								case 6:
									bricks.get(i).setTexture("res/textures/drops/fastballs.png", 16, 16);
									break;
								case 7:
									bricks.get(i).setTexture("res/textures/drops/slowballs.png", 16, 16);
									break;
								case 8:
									bricks.get(i).setTexture("res/textures/drops/expanddrop.png", 16, 16);
									break;
								case 9:
									bricks.get(i).setTexture("res/textures/drops/shrinkdrop.png", 16, 16);
									break;
								case 10:
									bricks.get(i).setTexture("res/textures/drops/10points.png", 16, 16);
									break;
								case 11:
									bricks.get(i).setTexture("res/textures/drops/20points.png", 16, 16);
									break;
								case 12:
									bricks.get(i).setTexture("res/textures/drops/ballshrink.png", 16, 16);
									break;
								case 13:
									bricks.get(i).setTexture("res/textures/drops/ballgrow.png", 16, 16);
									break;
								default:
									bricks.get(i).setTexture("res/textures/drops/drops.png", 16, 16);
									break;
							}
							
							drops.add(this.bricks.get(i));
						}
							bricks.remove(i);
					}
					
					collision = 1;
				}
		}
		
		return collision;
	}
	
	public int UpdateDrops(Bat bat) {
		int id = -1;
		int q = 0;
		
			for (Brick i : drops){
				i.updatePosition();
				
				if (collision(bat, i)) {
					id = i.getId();
					if(this.drops.remove(q) != null) {
						break;
					}	
					q++;
				}
			}
		
		return id;
	}
	
	private boolean collision(final Entity one, final Entity two) {
		return ((one.getYPosition() + one.getHeight()) <= two.getYPosition() || one.getYPosition() >= (two.getYPosition() + two.getHeight()) || (one.getXPosition() + one.getWidth()) <= two.getXPosition() || one.getXPosition() >= (two.getXPosition() + two.getHeight()) ) ? false : true;
	}

	public boolean anyBricks() {
		return (bricks.size() - amountOfIndestrucableBricks > 0) ? false : true;
	}

}
