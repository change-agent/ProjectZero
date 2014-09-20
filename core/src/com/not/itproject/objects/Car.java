package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Car extends GameObject{
	
	// Static properties used for car handling
	private static final float MAX_ANGLE = (float) (Math.PI / 3);
	private static final float STEER_SPEED = 2.0f;
	private static final float SIDE_FRICTION = 5.0f;
	private static final float HORSEPOWER = 60.0f;
	private static final float DRIFT_COEFF = 1.25f;
	private static final float LINEAR_FRICTION = 5.0f;
	private static final float PPM = 8.0f; //pixels per meter
	private static final float DENSITY = 2.0f;
	
	// Variable properties ued for car handling
	private Rectangle bounds; // don't forget to update bounds too
	private Body chassis;
	private float engineSpeed = 0.0f;
	private float steeringAngle = 0.0f;
	
	// Box2D box used for applying forces and collision handling
	public Car(World worldBox2D, float x, float y, float width, float height, float rotation) {
		super(worldBox2D, x, y, width, height, rotation);
		
		// define initial starting conditions
		this.bounds = new Rectangle(x, y, width, height);
		
		// Set up the physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set( new Vector2(x, y) );
		bodyDef.linearDamping = 2.0f;
		bodyDef.angularDamping = 2.0f;
		
		// Create the chassis shape and associated fixture
		PolygonShape chasisShape = new PolygonShape();
		chasisShape.setAsBox(width/2, height/2);
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = DENSITY;
		fixDef.friction = LINEAR_FRICTION;
		fixDef.shape = chasisShape;
		
		// Create the car chassis in the world of box2D
		chassis = worldBox2D.createBody(bodyDef);
		chassis.createFixture(fixDef);
		
		// Create each wheel
	}
	
	public void update(float delta) {
		position = chassis.getPosition();
		rotation = chassis.getAngle() * (float)(180 / Math.PI);
		bounds.x = position.x;
		bounds.y = position.y;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getVelocity() {
		return chassis.getLinearVelocity();
	}
	
	public void applyForce(float force) {
		chassis.applyForceToCenter(new Vector2(0, force * PPM), true);
		System.out.println(chassis.getLinearVelocity().y);
	}

	public void applyTorque(float rotation) {
		
	}
	
	public void killRightVelocity() {
		
	}


}
