package com.not.itproject.objects;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.not.itproject.objects.GameObject.ObjType;

public class PowerUp extends GameObject {

	private static final float SPAWNTIME = 3;
	private static final Random rand = new Random();
	private static enum PowerType {SPEEDBOOST, SPEEDREDUCE}

	// Variable object types
    private Body body;
	private PowerType powerType;
	public float coolDown;
	private Boolean collected; // check this when rendering, only render if false
	
	/** ------------------------- START CONSTRUCTOR --------------------- **/
	public PowerUp(World worldBox2D, float x, float y, 
			float width, float height, float rotation) 
	{
		// Call super constructor
		super(worldBox2D, x, y, width, height, rotation);
		
		this.powerType = setRandomPower();
		this.collected = false;
		this.coolDown = 0;
		this.objType = ObjType.POWER;

		// Define the box2D body for this power
		BodyDef box = new BodyDef();
		box.type = BodyType.StaticBody;
		box.position.set(new Vector2(x, y));
		
		// Define the box2d shape attached to body
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width/2, height/2);
		
		// Give the shape a fixture for physics handling
		FixtureDef boxFixDef = new FixtureDef();
		boxFixDef.density = 1.0f;
		boxFixDef.shape = boxShape;
		boxFixDef.isSensor = true;
		boxFixDef.filter.categoryBits = objType.value();
		
		// Place the object in the world
		this.body = worldBox2D.createBody(box);
		body.createFixture(boxFixDef);
		body.setUserData(this);
	}
	/** ----------------------------- END CONSTRUCTOR ----------------------- **/
	
	/** ----------------------------- START UPDATE -------------------------- **/
	public void update(float delta) 
	{
		if(collected) 
		{
			coolDown = Math.max(coolDown - delta, 0);
		}
		if(coolDown == 0) 
		{
			reSpawn();
		}
	}
	/** ----------------------------- END UPDATE --------------------------- **/

	/** ----------------------------- START METHODS ------------------------ **/
	// Check to see if the power is currently in a cool down period
	// Used by game renderer
	public Boolean isCoolingDown() 
	{
		if(coolDown > 0) 
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	// Re-spawns a power once the cool down period is over
	public void reSpawn()
	{
		powerType = setRandomPower();
		collected = false;
		Filter unmask = new Filter();
		unmask.maskBits = (short) 0xFFFF;
		body.getFixtureList().get(0).setFilterData(unmask);
		coolDown = 0; //just to be safe
	}
	
	public void collect() 
	{
		coolDown = SPAWNTIME;
		collected = true;
	}
	
	// Sets a random power from the enum type
	private PowerType setRandomPower() 
	{
		int randPower = rand.nextInt(PowerType.values().length);
		return PowerType.values()[randPower];
	}
		
	// Applies power to a specific player
	public void applyPower(Player player, List<Player> opponents) 
	{
		if(powerType == PowerType.SPEEDBOOST)
		{
			Vector2 velocity = player.getCar().getVelocity();
			player.getCar().setVelocity(velocity.scl(2.0f));
			System.out.println(velocity.scl(1.2f).x);
			System.out.println(velocity.scl(1.2f).y);
		}
		if(powerType == PowerType.SPEEDREDUCE)
		{
			for (Player opponent : opponents) {
				Vector2 velocity = opponent.getCar().getVelocity();
				if(opponent == player) {continue;}
				opponent.getCar().setVelocity(velocity.scl(0.5f));
			}
		}
	}
	/** ----------------------------- END METHODS ------------------------- **/
	
	/** ------------------------ DEBUG UNIT TESTING  ---------------------- **/
	/* DEBUG: test set a random power from the enum type */
	public void testSetRandPower() 
	{
		PowerType test = setRandomPower();
		System.out.print("New power type: " + test);
	}
	
	/* DEBUG: test collecting power*/
	public void testCollect() 
	{
		collect();
		System.out.print("Item collected!\n");
		System.out.print("Collected: " + collected);
		System.out.print("Wait time: " + coolDown);
	}
	
	/* DEBUG: test respawning a power */
	public void testRespawn() 
	{
		reSpawn();
		System.out.print("Item spawned!\n");
		System.out.print("Collected: " + collected);
		System.out.print("Wait time: " + coolDown);
		System.out.print("New power type: " + powerType);
	}
	/** --------------------- END DEBUG UNIT TESTING  ------------------ **/
}
