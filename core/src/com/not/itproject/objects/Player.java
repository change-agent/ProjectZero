package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject {
	Rectangle bounds;
	Vector2 velocity;
	int lapNum;
	boolean isHit;
	boolean hasPower;
	PowerUp power;
	
	// main constructor
	public Player(float x, float y, float width, float height, float rotation) {
		// define super
		super(x, y, width, height, rotation);
		
		// define bounds
		bounds = new Rectangle(x, y, width, height);
		velocity = new Vector2(0, 0);
		isHit = false;
		hasPower = false;
		power = null;
	}
	
	public void update(float delta) {
		
	}
	
	public void usePower() {
		
	}
	
	public void moveLeft(float value, float delta) {
		// move left with rotation
		position.x -= value * delta;
		rotation -= value * delta;
	}
	
	public void moveRight(float value, float delta) {
		// move right with rotation
		position.x += value * delta;
		rotation += value * delta;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getLapNum() {
		return lapNum;
	}
	
	/**
	 * @return the velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}


}
