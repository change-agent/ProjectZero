package com.not.itproject.objects;

public class Player {
	private Car car;
	private int lapNum;
	private boolean isHit;
	private boolean hasPower;
	private PowerUp power;
	
	
	// main constructor
	public Player(GameWorld gameWorld, float x, float y, float width, float height, float rotation) {	
		car = new Car(gameWorld, x, y, width, height, 0);
		isHit = false;
		hasPower = false;
		power = null;
	}
	
	public void update(float delta) {
		car.update(delta);
	}
	
	public void usePower() {
		
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
}
