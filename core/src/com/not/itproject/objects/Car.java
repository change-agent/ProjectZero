package com.not.itproject.objects;

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
	/** ----------------------------- CONSTANTS ------------------------------ **/
	// Static properties used for car handling
	private static final float LOCK_ANGLE = 45 * DEG_TO_RAD;
	private static final float STEER_SPEED = 6.0f;
	private static final float HORSEPOWER = 450.0f;
	private static final float DRIFT_COEFF = 0.4f; // Decrease for more skid
	private static final float LINEAR_FRICTION = 1.0f;
	private static final float CHASSIS_DENSITY = 2.0f;
	private static final float WHEEL_DENSITY = 1.0f;
	public static final float WHEEL_WIDTH = 2.5f;
	public static final float WHEEL_HEIGHT = 4.0f;
	
	/** ----------------------------- VARIABLES ------------------------------ **/
	// Variable properties ued for car handling
	private Body chassis, leftFrontWheel, rightFrontWheel, leftRearWheel, rightRearWheel;
	private RevoluteJoint leftFrontWheelJoint, rightFrontWheelJoint;
	private float enginePower = 0.0f;
	private float steeringAngle = 0.0f;
	private PowerUp power;
	private boolean hasPower;
	private boolean usePower;
	
	/** ----------------------------- START CONSTRUCTOR ----------------------- **/
	// Box2D box used for applying forces and collision handling
	public Car(World worldBox2D, float x, float y, 
			float width, float height, float rotation) {
		super(worldBox2D, x, y, width, height, rotation);
	
		// Set up the object and power variables
		this.power = null;
		this.hasPower = false;
		this.objType = ObjType.CAR;
		this.usePower = false;
		
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
		Vector2 leftFrontWheelPos = new Vector2(-width / 2 + WHEEL_WIDTH / 4,
				-height / 2 + WHEEL_HEIGHT / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 leftRearWheelPos = new Vector2(-width / 2 + WHEEL_WIDTH / 4 ,
				height / 2 - WHEEL_HEIGHT / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 rightRearWheelPos = new Vector2(width / 2 - WHEEL_WIDTH / 4,
				height / 2 - WHEEL_HEIGHT / 1.5f).scl(PIXELS_TO_METERS);
		Vector2 rightFrontWheelPos = new Vector2(width / 2 - WHEEL_WIDTH / 4,
				-height / 2 + WHEEL_HEIGHT / 1.5f).scl(PIXELS_TO_METERS);
				
		// Define the wheel shape and fixture
		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(WHEEL_WIDTH / 2 * PIXELS_TO_METERS, WHEEL_HEIGHT / 2 * PIXELS_TO_METERS);
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
		leftFrontJointDef.maxMotorTorque = 200;
		leftFrontJointDef.lowerAngle = -1 * LOCK_ANGLE;
		leftFrontJointDef.upperAngle = LOCK_ANGLE;
		
		RevoluteJointDef rightFrontJointDef = new RevoluteJointDef();
		rightFrontJointDef.initialize(
				chassis, rightFrontWheel, rightFrontWheel.getWorldCenter());
		rightFrontJointDef.enableMotor = true;
		rightFrontJointDef.enableLimit = true;
		rightFrontJointDef.maxMotorTorque = 200;
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
	/** ----------------------------- END UPDATE -------------------------------- **/
	
	/** ----------------------- CAR HANDLING FUNCTIONS -------------------------- **/
	// Prevents the car from rotating about center and travelling orthogonally
	public void killRightVelocity(Body wheel) {
		// Prevent too much skidding by killing orthogonal
		Vector2 currRightNorm = wheel.getWorldVector( new Vector2(1, 0) );
		Vector2 orhthogAmount = currRightNorm.scl(currRightNorm.dot(wheel.getLinearVelocity()));
		Vector2 impulse = orhthogAmount.scl(wheel.getMass() * -DRIFT_COEFF);
		wheel.applyLinearImpulse(impulse, wheel.getWorldCenter(), true);
		
		// Prevent too much spot - rotation by killing the angular velocity
		float angleImpulse = 0.1f * wheel.getInertia() * -DRIFT_COEFF;
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
		enginePower += direction * HORSEPOWER * 0.05;
		enginePower = clamp(enginePower, -HORSEPOWER, HORSEPOWER);
	}
	
	public void setSteeringAngle(float steerAmount) {
		steeringAngle = steerAmount * LOCK_ANGLE;
	}
	
	public void powerOffEngine() {
		// Kills the engine slowly to give the effect of drag slow-down
		enginePower /= 1.04;
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
	
	public void setVelocity(Vector2 velocity) {
		chassis.setLinearVelocity(velocity);
	}
	
	public void setDensity(float scale) {
		chassis.getFixtureList().get(0).setDensity(CHASSIS_DENSITY * scale);
		leftFrontWheel.getFixtureList().get(0).setDensity(CHASSIS_DENSITY * scale);
		leftRearWheel.getFixtureList().get(0).setDensity(CHASSIS_DENSITY * scale);
		rightFrontWheel.getFixtureList().get(0).setDensity(CHASSIS_DENSITY * scale);
		rightRearWheel.getFixtureList().get(0).setDensity(CHASSIS_DENSITY * scale);
	}
	/** ------------------------ END UTILITY FUNCTIONS -------------------------- **/
}
