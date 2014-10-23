package com.not.itproject.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PowerUpContainer extends GameObject
{
	private final float SPAWNTIME = 5;
	private Body body;
	private PowerUp powerUp;
	private float cooldown;
	
	/** ------------------------- START CONSTRUCTOR --------------------- **/
	public PowerUpContainer(World worldBox2D, float x, float y, float width,
			float height, float rotation) {
		super(worldBox2D, x, y, width, height, rotation);
		
		this.cooldown = 0;
		this.objType = ObjType.POWER_UP_CONTAINER;
		this.powerUp = new PowerUp();
		
		// Define the box2D body for this power
		BodyDef box = new BodyDef();
		box.type = BodyType.StaticBody;
		box.position.set(new Vector2(x, y));
		
		// Define the box2d shape attached to body
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width/2 * PIXELS_TO_METERS, height/2 * PIXELS_TO_METERS);
		
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
	public void update(float delta) {
		updateCooldown(delta);
		if(!isCoolingDown()) {
			respawn();
		}
	}
	/** ----------------------------- END UPDATE --------------------------- **/

	/** ----------------------------- START METHODS ------------------------ **/
	// Resets the cooldown during respawn call
	private void resetCooldown() {
		cooldown = 0;
	}

	// Check to see if the power is currently in a cool down period
	// Used by game renderer
	public boolean isCoolingDown() {
		if(cooldown > 0) 
		{
			return true;
		}
		else {
			return false;
		}
	}

	// Starts the cooldown when a power is collected
	private void startCooldown() {
		cooldown = SPAWNTIME;	
	}
	
	public void CollectPowerUp() {
		// Select a dummy power (safer than null)
		powerUp = null;
		body.setUserData(this);
		startCooldown();
		
		// mask body to prevent sensory detection
		Filter unmask = new Filter();
		unmask.maskBits = (short) ObjType.POWER_UP_CONTAINER.value();
		body.getFixtureList().get(0).setFilterData(unmask);
	}

	// Re-spawns a power once the cool down period is over
	private void respawn() {
		//Select new power-up
		powerUp = new PowerUp();
		body.setUserData(this);
		resetCooldown();
		
		// Unmask to allow for sensory detection again
		Filter unmask = new Filter();
		unmask.maskBits = (short) 0xFFFF;
		body.getFixtureList().get(0).setFilterData(unmask);
	}

	// Update the value of the cooldown timer per frame
	private void updateCooldown(float delta) {
		cooldown = Math.max(0.0f, cooldown - delta);
	}
	
	public PowerUp getPowerUp() {
		return powerUp;
	}
	/** ----------------------------- END METHODS ------------------------- **/
}