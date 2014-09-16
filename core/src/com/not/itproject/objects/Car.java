package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Car extends GameObject{
	
	// Properties used for car handling
	private static final float MAX_VEL = 10;
	private static final float MAX_ACCEL = 2;
	private static final float MAX_ROTATION = 10;
	private static final float DRIFT_COEFF = 2;
	private static final float FRICTION = 5;
	
	private Rectangle bounds; // don't forget to update bounds too
	private Vector2 velocity;
	private Vector2 acceleration;
	private Body chasis;
	private Vector2 centerOfMass = new Vector2(width/2, height/2);
	
	// Box2D box used for applying forces and collision handling

	public Car(GameWorld gameWorld, float x, float y, float width, float height, float rotation) {
		super(gameWorld, x, y, width, height, rotation);
		
		// define initial starting conditions
		this.bounds = new Rectangle(x, y, width, height);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
//		
//		// Set up the physics body
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.type = BodyType.DynamicBody;
//		
//		PolygonShape chasisShape = new PolygonShape();
//		chasisShape.setAsBox(width/2, height/2, centerOfMass, rotation);
//		
//		FixtureDef fixDef = new FixtureDef();
//		fixDef.friction = gameWorld.FRICTION;
//		fixDef.shape = chasisShape;
//
//		gameWorld.worldBox2D.createBody(bodyDef);
//		chasis.createFixture(fixDef);
		
		
	}
	
	public void update(float delta) {
		
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	

	public void applyForce(float force) {
		// TODO Auto-generated method stub
		
	}

	public void applyTorque(float rotation) {
		// TODO Auto-generated method stub
		
	}
	
	public void killRightVelocity() {
		
	}


}
