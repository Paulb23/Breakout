package org.BrokenPixel.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class Keyboards {
	
	
	public transient boolean left, right, up, down, tab,  shoot, select, menu, pause, mute;
	
	public Keyboards() {
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Keyboard.enableRepeatEvents(false);
	}
	
	public void update() {
		while (Keyboard.next()){
			if (Keyboard.getEventKeyState()) {  
				if(Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					  left = true;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					  right = true;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_UP) {
					  up = true;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					  down = true;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_TAB) {
					  tab = true;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					  shoot = true;
				 } 
	
				 if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					  select = false;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					  menu = true;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_P) {
					  pause = true;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_M) {
					  mute = true;
				 }
			} else {
				if(Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					  left = false;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					  right = false;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_UP) {
					  up = false;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					  down = false;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_TAB) {
					  tab = false;
				 }
				 
				 if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					  shoot = false;
				 } 
	
				 if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					  select = true;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					  menu = false;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_P) {
					  pause = false;
				 }
				  
				 if(Keyboard.getEventKey() == Keyboard.KEY_M) {
					  mute = false;
				 }
			}
		}
	}
}
