package com.not.itproject.objects;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PowerUp extends GameObject {

	private static final float SPAWNTIME = 3;
	private static final Random rand = new Random();
	private enum PowerType {SPEEDBOOST, SPEEDREDUCE};
	
	// Variable object types
	private PowerType type;
	Rectangle bounds;
	private float wait;
	private Boolean collected; // check this when rendering, only render if false
	
	public PowerUp(World worldBox2D, float x, float y, float width, float height, float rotation) {
		// define super
		super(worldBox2D, x, y, width, height, rotation);
		type = setRandomPower();
		bounds = new Rectangle(x, y, width, height);
		collected = false;
		wait = 0;
	}
	
	public void update(float delta) {
		if(collected) {
			wait = Math.max(wait - delta, 0);
		}
		if(wait == 0) {
			reSpawn();
		}
	}

	/* Setters and getters */
	public Boolean isColleted() {
		return collected;
	}
	
	/* Actions functions to effect world */
	public void reSpawn() {
		type = setRandomPower();
		collected = false;
		wait = 0; //just to be safe
	}
	
	public void collect() {
		wait = SPAWNTIME;
		collected = true;
	}
	
	/* Sets a random power from the enum type */
	private PowerType setRandomPower() {
		int randPower = rand.nextInt(PowerType.values().length);
		return PowerType.values()[randPower];
	}
	
	/* Applies power to a specific player - maybe we should move this to game world*/
	public void applyPower(Player player) {
		Vector2 playerVel = player.getCar().getVelocity(); 
		if(type == PowerType.SPEEDBOOST){
			//player.getCar().setVelocity(playerVel.scl(2.0f));
		}
		if(type == PowerType.SPEEDREDUCE){
			//player.getCar().setVelocity(playerVel.scl(0.5f));
		}
	}
	
	/* DEBUG: test set a random power from the enum type */
	public void testSetRandPower() {
		PowerType test = setRandomPower();
		System.out.print("New power type: " + test);
	}
	
	/* DEBUG: test collecting power*/
	public void testCollect() {
		collect();
		System.out.print("Item collected!\n");
		System.out.print("Collected: " + collected);
		System.out.print("Wait time: " + wait);
	}
	
	/* DEBUG: test respawning a power */
	public void testRespawn() {
		reSpawn();
		System.out.print("Item spawned!\n");
		System.out.print("Collected: " + collected);
		System.out.print("Wait time: " + wait);
		System.out.print("New power type: " + type);
	}
}
