package com.not.itproject.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Car extends GameObject{	
	/** ----------------------------- CONSTANTS ------------------------------ **/
	// Static properties used for car handling
	private final float LOCK_ANGLE = 35 * DEG_TO_RAD;
	private final float STEER_SPEED = 0.8f;
	private final float LINEAR_FRICTION = 2.0f;
	private final float CHASSIS_DENSITY = 3.0f;
	private final float WHEEL_DENSITY = 1.8f;
	private float maxHorsePower = 600;
	private float minHorsePower = 400;
	private float minDriftCoeff = 0.2f;
	private float maxDriftCoeff = 0.6f;
	private float driftCoeff = 0.2f; // Decrease for more skid
	
	/** ----------------------------- VARIABLES ------------------------------ **/
	// Variable properties ued for car handling
	private Body chassis, leftFrontWheel, rightFrontWheel, leftRearWheel, rightRearWheel;
	private RevoluteJoint leftFrontWheelJoint, rightFrontWheelJoint;
	private float enginePower = 0.0f;
	private float steeringAngle = 0.0f;
	private float horsepower = maxHorsePower;
	private PowerUp power;
	private boolean hasPower;
	private boolean usePower;
	private float wheelWidth = width / 8;
	private float wheelHeight = height / 8;
	private Player owner;
	
	/** ----------------------------- START CONSTRUCTOR ----------------------- **/
	// Box2D box used for applying forces and collision handling
	public Car(World worldBox2D, Player owner, float x, float y, 
			float width, float height, float rotation) {
		super(worldBox2D, x, y, width, height, rotation);
	
		// Set up the object and power variables
		this.power = null;
		this.hasPower = false;
		this.objType = ObjType.CAR;
		this.usePower = false;
		this.owner = owner;
		
		// Set up the physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set( new Vector2(x, y) );
		bodyDef.linearDamping = 1.0f;
		bodyDef.angularDamping = 2.0f;
		
		// Create the chassis shape and associated fixture
		PolygonShape chasisShape = new PolygonShape();
		chasisShape.setAsBox(width/2 * PIXELS_TO_METERS, height/2 * PIXELS_TO_METERS);
		FixtureDef chassisFixDef = new FixtureDef();
		chassisFixDef.density = CHASSIS_DENSITY;
		chassisFixDef.friction = 0;
		chassisFixDef.shape = chasisShape;
		chassisFixDef.filter.categoryBits = objType.value();
		
		// Create the car chassis in the world of box2D
		this.chassis = worldBox2D.createBody(bodyDef);
		chassis.createFixture(chassisFixDef);
		
		// Relative position of each wheel from the chassis
		Vector2 leftFrontWheelPos = new Vector2(-width / 2 + wheelWidth / 4,
				-height / 2 + wheelHeight / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 leftRearWheelPos = new Vector2(-width / 2 + wheelWidth / 4 ,
				height / 2 - wheelHeight / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 rightRearWheelPos = new Vector2(width / 2 - wheelWidth / 4,
				height / 2 - wheelHeight / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 rightFrontWheelPos = new Vector2(width / 2 - wheelWidth / 4,
				-height / 2 + wheelHeight / 1.5f).scl(PIXELS_TO_METERS);
				
		// Define the wheel shape and fixture
		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(wheelWidth / 2 * PIXELS_TO_METERS, wheelHeight / 2 * PIXELS_TO_METERS);
		FixtureDef wheelFixDef = new FixtureDef();
		wheelFixDef.density = WHEEL_DENSITY;
		wheelFixDef.friction = LINEAR_FRICTION;
		wheelFixDef.shape = wheelShape;
		wheelFixDef.filter.categoryBits = objType.value();
		
		// Create a shape for each wheel
		BodyDef leftFrontWheelBodyDef = new BodyDef();
		leftFrontWheelBodyDef.type = BodyType.DynamicBody;
		leftFrontWheelBodyDef.linearDamping = 1.0f;
		leftFrontWheelBodyDef.angularDamping = 2.0f;
		leftFrontWheelBodyDef.position.set(
				chassis.getPosition().add(leftFrontWheelPos));
		this.leftFrontWheel = worldBox2D.createBody(leftFrontWheelBodyDef);
		leftFrontWheel.createFixture(wheelFixDef);
		
		BodyDef rightFrontWheelBodyDef = new BodyDef();
		rightFrontWheelBodyDef.type = BodyType.DynamicBody;
		rightFrontWheelBodyDef.linearDamping = 1.0f;
		rightFrontWheelBodyDef.angularDamping = 2.0f;
		rightFrontWheelBodyDef.position.set(
				chassis.getPosition().add(rightFrontWheelPos));
		this.rightFrontWheel = worldBox2D.createBody(rightFrontWheelBodyDef);
		rightFrontWheel.createFixture(wheelFixDef);
		
		BodyDef leftRearWheelBodyDef = new BodyDef();
		leftRearWheelBodyDef.type = BodyType.DynamicBody;
		leftRearWheelBodyDef.position.set(
				chassis.getPosition().add(leftRearWheelPos));
		this.leftRearWheel = worldBox2D.createBody(leftRearWheelBodyDef);
		leftRearWheel.createFixture(wheelFixDef);
		
		BodyDef rightRearWheelBodyDef = new BodyDef();
		rightRearWheelBodyDef.type = BodyType.DynamicBody;
		rightRearWheelBodyDef.position.set(
				chassis.getPosition().add(rightRearWheelPos));
		this.rightRearWheel = worldBox2D.createBody(rightRearWheelBodyDef);
		rightRearWheel.createFixture(wheelFixDef);
		
		//Create the joints to attach to body
		RevoluteJointDef leftFrontJointDef = new RevoluteJointDef();
		leftFrontJointDef.initialize(
				chassis, leftFrontWheel, leftFrontWheel.getWorldCenter());
		leftFrontJointDef.enableMotor = true;
		leftFrontJointDef.enableLimit = true;
		leftFrontJointDef.maxMotorTorque = 125;
		leftFrontJointDef.lowerAngle = -1 * LOCK_ANGLE;
		leftFrontJointDef.upperAngle = LOCK_ANGLE;
		
		RevoluteJointDef rightFrontJointDef = new RevoluteJointDef();
		rightFrontJointDef.initialize(
				chassis, rightFrontWheel, rightFrontWheel.getWorldCenter());
		rightFrontJointDef.enableMotor = true;
		rightFrontJointDef.enableLimit = true;
		rightFrontJointDef.maxMotorTorque = 125;
		rightFrontJointDef.lowerAngle = -1 * LOCK_ANGLE;
		rightFrontJointDef.upperAngle = LOCK_ANGLE;
		
		PrismaticJointDef leftRearJointDef = new PrismaticJointDef();
		leftRearJointDef.initialize(chassis, leftRearWheel, 
				leftRearWheel.getWorldCenter(), new Vector2(1.0f, 0));
		leftRearJointDef.enableLimit = true;
		leftRearJointDef.lowerTranslation = 0; 
		leftRearJointDef.upperTranslation = 0;
		
		PrismaticJointDef rightRearJointDef = new PrismaticJointDef();
		rightRearJointDef.initialize(chassis, rightRearWheel, 
				rightRearWheel.getWorldCenter(), new Vector2(1.0f, 0));
		rightRearJointDef.enableLimit = true;
		rightRearJointDef.lowerTranslation = 0; 
		rightRearJointDef.upperTranslation = 0;
		
		this.leftFrontWheelJoint = (RevoluteJoint) worldBox2D.createJoint(leftFrontJointDef);
		this.rightFrontWheelJoint = (RevoluteJoint) worldBox2D.createJoint(rightFrontJointDef);
		worldBox2D.createJoint(leftRearJointDef);
		worldBox2D.createJoint(rightRearJointDef);
		
		// Sets the bodies to hold a reference to the object they belong to
		// This is used in the contact listener
		chassis.setUserData(this);
		leftFrontWheel.setUserData(this);
		leftRearWheel.setUserData(this);
		rightFrontWheel.setUserData(this);
		rightRearWheel.setUserData(this);
	}
	/** ----------------------------- END CONSTRUCTOR --------------------------- **/
	
	/** ------------------------------ START UPDATE ----------------------------- **/
	public void update(float delta) {
		// Kill the orthogonal wheel velocity to make steering more linear vel
		killRightVelocity(chassis);
		killRightVelocity(leftFrontWheel);
		killRightVelocity(leftRearWheel);
		killRightVelocity(rightFrontWheel);
		killRightVelocity(rightRearWheel);
		
		// Update the steering of the wheels
		float leftTurnSpeed, rightTurnSpeed;
		leftTurnSpeed = steeringAngle - leftFrontWheelJoint.getJointAngle();
		rightTurnSpeed = steeringAngle - rightFrontWheelJoint.getJointAngle();
		leftFrontWheelJoint.setMotorSpeed(delta * leftTurnSpeed); 
		rightFrontWheelJoint.setMotorSpeed(delta * rightTurnSpeed);
		leftFrontWheelJoint.setLimits(steeringAngle, steeringAngle);
		rightFrontWheelJoint.setLimits(steeringAngle, steeringAngle);
		
		// Update the forward movement from engine - this doesn't really work yet
		// Need to find replacements for a few functions that have been depreciated
		applyForwardForce(leftFrontWheel, enginePower);
		applyForwardForce(rightFrontWheel, enginePower);
		
		// Update the sprite position and rotation based on chassis
		position = chassis.getPosition();
		rotation = chassis.getAngle() * RAD_TO_DEG;
	}
	
	public void applyMovement(Vector2 position, Vector2 velocity, float rotation) {	
		// apply movement via position and velocity
		if (!worldBox2D.isLocked()) {
			// box2d world is not locked
			chassis.setTransform(position, rotation * DEG_TO_RAD);
			chassis.setLinearVelocity(velocity);
		}
	}
	/** ----------------------------- END UPDATE -------------------------------- **/
	
	/** ----------------------- CAR HANDLING FUNCTIONS -------------------------- **/
	// Prevents the car from rotating about center and travelling orthogonally
	public void killRightVelocity(Body wheel) {
		// Prevent too much skidding by killing orthogonal
		Vector2 currRightNorm = wheel.getWorldVector( new Vector2(1, 0) );
		Vector2 orhthogAmount = currRightNorm.scl(currRightNorm.dot(wheel.getLinearVelocity()));
		Vector2 impulse = orhthogAmount.scl(0.2f * wheel.getMass() * -driftCoeff);
		wheel.applyLinearImpulse(impulse, wheel.getWorldCenter(), true);
		
		// Prevent too much spot - rotation by killing the angular velocity
		float angleImpulse = 0.8f * wheel.getInertia() * -driftCoeff;
		wheel.applyAngularImpulse(wheel.getAngularVelocity() * angleImpulse, true);
	}
	
	// Applies a force in the local forward direction of the body
	private void applyForwardForce(Body body, float force) {
		Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1));
		forwardNormal.nor();
		body.applyForce(forwardNormal.scl(force), body.getWorldCenter(), true);
	}
	
	// These methods are used by the input handler to set engine power and steering angle
	public void powerOnEngine(int direction) {
		enginePower += direction * horsepower * 0.05;
		enginePower = clamp(enginePower, -horsepower, horsepower);
	}
	
	public void setSteeringAngle(float steerAmount) {
		steeringAngle = STEER_SPEED * steerAmount * LOCK_ANGLE;
	}
	
	public void powerOffEngine() {
		// Kills the engine slowly to give the effect of drag slow-down
		enginePower /= 1.002;
	}
	
	public void powerOffEngine(boolean powerOff) {
		// Completely kills the engine
		if (powerOff) { 
			enginePower = 0; 
			Vector2 zeroVector = new Vector2(0, 0);
			chassis.setLinearVelocity(zeroVector);
			leftFrontWheel.setLinearVelocity(zeroVector); 
			rightFrontWheel.setLinearVelocity(zeroVector); 
			leftRearWheel.setLinearVelocity(zeroVector); 
			rightRearWheel.setLinearVelocity(zeroVector);
		}
	}
	
	public void zeroSteeringAngle() {
		steeringAngle = 0;
	}
	/** ------------------------END CAR HANDLING FUNCTIONS --------------------- **/
	
	/** ------------------------ POWERUP HANDLING FUNCTIONS -------------------- **/
	// These functions will be used to set and get the current held power
	public void setPower(PowerUp power) {
		this.power = power;
		hasPower = true;
	}
	
	public void setUsePower() {
		if(hasPower) {
			usePower = true;
		}
		else{
			usePower = false;
		}
	}
	
	public Boolean usePower() {
		return usePower;
	}
	
	public PowerUp getPower() {
		return power;
	}
	
	public void removePower() {
		power = null;
		hasPower = false;
	}
	/** ---------------------- END POWERUP HANDLING FUNCTIONS ------------------ **/
	
	/** -------------------------- UTILITY FUNCTIONS --------------------------- **/	
	// Clamps a float between min and max
	public float clamp(float val, float min, float max) {
		if(val < min) {
			return min;
		}
		else if(val > max) {
			return max;
		}
		else {
			return val;
		}
	}
	
	public Vector2 getVelocity() {
		return chassis.getLinearVelocity();
	}
	
	public float getEnginePower() {
		return enginePower;
	}
	
	public float getSteeringAngle() {
		return steeringAngle;
	}
	
	public void setVelocity(Vector2 velocity) {
		chassis.setLinearVelocity(velocity);
	}
	
	public void setMaxEnginePower(float scale) {
		maxHorsePower *= scale;
	}
	
	public Player getOwner() {
		return owner;
	}
	public void setFriction(float scale) {
		driftCoeff *= scale;
		driftCoeff = MathUtils.clamp(driftCoeff, minDriftCoeff, maxDriftCoeff);
		
		horsepower *= 1/scale;
		horsepower = MathUtils.clamp(horsepower, minHorsePower, maxHorsePower);
	}
	
	public void setMaskData(short maskBits) {
		Filter newMask = new Filter();
		newMask.maskBits = maskBits;
		chassis.getFixtureList().get(0).setFilterData(newMask);
		leftFrontWheel.getFixtureList().get(0).setFilterData(newMask);
		leftRearWheel.getFixtureList().get(0).setFilterData(newMask);
		rightFrontWheel.getFixtureList().get(0).setFilterData(newMask);
		rightRearWheel.getFixtureList().get(0).setFilterData(newMask);
	}
	/** ------------------------ END UTILITY FUNCTIONS -------------------------- **/
}
