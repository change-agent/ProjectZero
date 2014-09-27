package com.not.itproject.objects;

import com.badlogic.gdx.physics.box2d.World;

public class Player {
	private Car car;
	private String playerID;
	private int lapNum;
	
	// main constructor
	public Player(World worldBox2D, String playerID, float x, float y, float width, float height, float rotation) {	
		car = new Car(worldBox2D, x, y, width, height, 0);
		this.playerID = playerID;
	}
	
	public void update(float delta) {
		car.update(delta);
	}
		
	public Car getCar() {
		return car;
	}
	
	public String getPlayerID() {
		return playerID;
	}
	
	public int getLapNum() {
		return lapNum;
	}

	public void flagUsePower() {
		getCar().setUsePower();
	}
}