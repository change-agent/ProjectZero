package com.not.itproject.objects;

import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Car extends GameObject{
	
	// Static properties used for car handling
	private static final float MAX_ANGLE = (float) (Math.PI / 3);
	private static final float STEER_SPEED = 4.0f;
	private static final float SIDE_FRICTION = 10.0f;
	private static final float HORSEPOWER = 400.0f;
	private static final float DRIFT_COEFF = 1.25f;
	private static final float LINEAR_FRICTION = 5.0f;
	private static final float PPM = 8.0f; //pixels per meter
	private static final float DENSITY = 1.0f;
	public static final float WHEEL_WIDTH = 2.5f;
	public static final float WHEEL_HEIGHT = 4.0f;		
	
	// Variable properties ued for car handling
	private Rectangle bounds; // don't forget to update bounds too
	private Body chassis, leftFrontWheel, rightFrontWheel, leftRearWheel, rightRearWheel;
	private RevoluteJoint leftFrontWheelJoint, rightFrontWheelJoint;
	private float enginePower = 0.0f;
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
		bodyDef.linearDamping = 1.0f;
		bodyDef.angularDamping = 1.0f;
		
		// Create the chassis shape and associated fixture
		PolygonShape chasisShape = new PolygonShape();
		chasisShape.setAsBox(width/2, height/2);
		FixtureDef chassisFixDef = new FixtureDef();
		chassisFixDef.density = DENSITY;
		chassisFixDef.friction = LINEAR_FRICTION;
		chassisFixDef.shape = chasisShape;
		
		// Create the car chassis in the world of box2D
		chassis = worldBox2D.createBody(bodyDef);
		chassis.createFixture(chassisFixDef);
		
		// Relative position of each wheel from the chassis
		Vector2 leftFrontWheelPos = new Vector2(-width / 2 - WHEEL_WIDTH / 2, -height / 2 + WHEEL_HEIGHT / 1.5f);
		Vector2 leftRearWheelPos = new Vector2(-width / 2 - WHEEL_WIDTH / 2 , height / 2 - WHEEL_HEIGHT / 1.5f);
		Vector2 rightRearWheelPos = new Vector2(width / 2 + WHEEL_WIDTH / 2, height / 2 - WHEEL_HEIGHT / 1.5f);
		Vector2 rightFrontWheelPos = new Vector2(width / 2 + WHEEL_WIDTH / 2, -height / 2 + WHEEL_HEIGHT / 1.5f);
				
		// Define the wheel shape and fixture
		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(WHEEL_WIDTH / 2, WHEEL_HEIGHT / 2);
		FixtureDef wheelFixDef = new FixtureDef();
		wheelFixDef.density = DENSITY;
		wheelFixDef.friction = SIDE_FRICTION;
		wheelFixDef.shape = wheelShape;
		
		// Create a shape for each wheel
		BodyDef leftFrontWheelBodyDef = new BodyDef();
		leftFrontWheelBodyDef.type = BodyType.DynamicBody;
		leftFrontWheelBodyDef.linearDamping = 4.0f;
		leftFrontWheelBodyDef.angularDamping = 4.0f;
		leftFrontWheelBodyDef.position.set(chassis.getPosition().add(leftFrontWheelPos));
		leftFrontWheel = worldBox2D.createBody(leftFrontWheelBodyDef);
		leftFrontWheel.createFixture(wheelFixDef);
		
		BodyDef rightFrontWheelBodyDef = new BodyDef();
		rightFrontWheelBodyDef.type = BodyType.DynamicBody;
		rightFrontWheelBodyDef.linearDamping = 4.0f;
		rightFrontWheelBodyDef.angularDamping = 4.0f;
		rightFrontWheelBodyDef.position.set(chassis.getPosition().add(rightFrontWheelPos));
		rightFrontWheel = worldBox2D.createBody(rightFrontWheelBodyDef);
		rightFrontWheel.createFixture(wheelFixDef);
		
		BodyDef leftRearWheelBodyDef = new BodyDef();
		leftRearWheelBodyDef.type = BodyType.DynamicBody;
		leftRearWheelBodyDef.position.set(chassis.getPosition().add(leftRearWheelPos));
		leftRearWheel = worldBox2D.createBody(leftRearWheelBodyDef);
		leftRearWheel.createFixture(wheelFixDef);
		
		BodyDef rightRearWheelBodyDef = new BodyDef();
		rightRearWheelBodyDef.type = BodyType.DynamicBody;
		rightRearWheelBodyDef.position.set(chassis.getPosition().add(rightRearWheelPos));
		rightRearWheel = worldBox2D.createBody(rightRearWheelBodyDef);
		rightRearWheel.createFixture(wheelFixDef);
		
		
		//Create the joints to attach to body
		RevoluteJointDef leftFrontJointDef = new RevoluteJointDef();
		leftFrontJointDef.initialize(chassis, leftFrontWheel, leftFrontWheel.getWorldCenter());
		leftFrontJointDef.enableMotor = true;
		leftFrontJointDef.maxMotorTorque = 125;
		
		RevoluteJointDef rightFrontJointDef = new RevoluteJointDef();
		rightFrontJointDef.initialize(chassis, rightFrontWheel, rightFrontWheel.getWorldCenter());
		rightFrontJointDef.enableMotor = true;
		rightFrontJointDef.maxMotorTorque = 125;
		
		PrismaticJointDef leftRearJointDef = new PrismaticJointDef();
		leftRearJointDef.initialize(chassis, leftRearWheel, leftRearWheel.getWorldCenter(), new Vector2(1.0f, 0));
		leftRearJointDef.enableLimit = true;
		leftRearJointDef.lowerTranslation = leftRearJointDef.upperTranslation = 0;
		
		PrismaticJointDef rightRearJointDef = new PrismaticJointDef();
		rightRearJointDef.initialize(chassis, rightRearWheel, rightRearWheel.getWorldCenter(), new Vector2(1.0f, 0));
		rightRearJointDef.enableLimit = true;
		rightRearJointDef.lowerTranslation = rightRearJointDef.upperTranslation = 0;
		
		leftFrontWheelJoint = (RevoluteJoint) worldBox2D.createJoint(leftFrontJointDef);
		rightFrontWheelJoint = (RevoluteJoint) worldBox2D.createJoint(rightFrontJointDef);
		worldBox2D.createJoint(leftRearJointDef);
		worldBox2D.createJoint(rightRearJointDef);
	}
	
	public void update(float delta) {
		
		// This updates the box2d world
		// Adjust last two params for performance increase
		worldBox2D.step(delta, 1, 1);
		
		// Kill the orthogonal wheel velocity to make steering more linear vel 
		killRightVelocity(leftFrontWheel);
		killRightVelocity(leftRearWheel);
		killRightVelocity(rightFrontWheel);
		killRightVelocity(rightRearWheel);
		
		// Update the steering of the wheels
		float leftTurnSpeed, rightTurnSpeed;
		leftTurnSpeed = steeringAngle - leftFrontWheelJoint.getJointAngle();
		rightTurnSpeed = steeringAngle - rightFrontWheelJoint.getJointAngle();
		leftFrontWheelJoint.setMotorSpeed(STEER_SPEED * leftTurnSpeed); 
		rightFrontWheelJoint.setMotorSpeed(STEER_SPEED * rightTurnSpeed);
		
		// Update the forward movement from engine - this doesn't really work yet
		// Need to find replacements for a few functions that have been depreciated
		float lAngle = leftFrontWheel.getAngle();
		float rAngle = rightFrontWheel.getAngle();
		Vector2 lDirection = new Vector2(0.0f, 1.0f);
		Vector2 rDirection = new Vector2(0.0f, 1.0f);
		lDirection.rotate(lAngle);
		rDirection.rotate(rAngle);
		leftFrontWheel.applyForceToCenter(lDirection.scl(PPM * enginePower), true);
		rightFrontWheel.applyForceToCenter(rDirection.scl(PPM * enginePower), true);
		
		// Update the sprite position and rotation based on chassis
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
	
	
	public void killRightVelocity(Body wheel) {
		
	}
	
	// These methods are used by the input handler to set engine power and steering angle
	public void powerOnEngine(int direction) {
		enginePower = direction * PPM * HORSEPOWER;
	}
	
	public void setSteeringAngle(float steerAngle) {
		steeringAngle = steerAngle * MAX_ANGLE;
	}
	
	public void powerOffEngine() {
		enginePower = 0;
	}
	
	public void zeroSteeringAngle() {
		steeringAngle = 0;
	}
}
