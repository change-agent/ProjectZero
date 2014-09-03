package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;

public class Player extends GameObject {
	Rectangle bounds;
	
	// main constructor
	public Player(float x, float y, float width, float height, float rotation) {
		// define super
		super(x, y, width, height, rotation);
		
		// define bounds
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void update(float delta) {
		
	}
	
	public void moveLeft(float value, float delta) {
		position.x -= value * delta;
	}
	
	public void moveRight(float value, float delta) {
		position.x += value * delta;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

}
