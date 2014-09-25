package com.not.itproject.objects;

import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.objects.GameObject.ObjType;

public class Player {
	private Car car;
	private int lapNum;
	private boolean isHit;
	
	// main constructor
	public Player(World worldBox2D, float x, float y, float width, float height, float rotation) {	
		car = new Car(worldBox2D, x, y, width, height, 0);
		isHit = false;
	}
	
	public void update(float delta) {
		car.update(delta);
	}
		
	public Car getCar() {
		return car;
	}
	
	public int getLapNum() {
		return lapNum;
	}

	public void setHit(boolean b) {
		isHit = b;	
	}

	public void flagUsePower() {
		getCar().setUsePower();
	}
}