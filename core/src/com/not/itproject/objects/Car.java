package com.not.itproject.objects;

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
	
	//Conversion function
	private static float DEG2RAD = (float)Math.PI / 180;
	private static float RAD2DEG = 1/DEG2RAD;
	
	// Static properties used for car handling
	private static final float LOCK_ANGLE = 15 * DEG2RAD;
	private static final float STEER_SPEED = 2.0f;
	private static final float HORSEPOWER = 1000.0f;
	private static final float DRIFT_COEFF = 0.5f; // Decrease for more skid
	private static final float LINEAR_FRICTION = 2.0f;
	private static final float PPM = 8.0f; //pixels per meter
	private static final float CHASSIS_DENSITY = 1.0f / (PPM * PPM);
	private static final float WHEEL_DENSITY = 0.8f / (PPM * PPM);
	public static final float WHEEL_WIDTH = 2.5f;
	public static final float WHEEL_HEIGHT = 4.0f;		
	
	// Variable properties ued for car handling
	private Rectangle bounds; // don't forget to update bounds too
	private Body obs, chassis, leftFrontWheel, rightFrontWheel, leftRearWheel, rightRearWheel;
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
		
		// Create the chassis shape and associated fixture
		PolygonShape chasisShape = new PolygonShape();
		chasisShape.setAsBox(width/2, height/2);
		FixtureDef chassisFixDef = new FixtureDef();
		chassisFixDef.density = CHASSIS_DENSITY;
		chassisFixDef.friction = 0;
		chassisFixDef.shape = chasisShape;
		
		// Create the car chassis in the world of box2D
		chassis = worldBox2D.createBody(bodyDef);
		chassis.createFixture(chassisFixDef);
		
		// Relative position of each wheel from the chassis
		Vector2 leftFrontWheelPos = new Vector2(-width / 2 + WHEEL_WIDTH / 4, -height / 2 + WHEEL_HEIGHT / 1.5f);
		Vector2 leftRearWheelPos = new Vector2(-width / 2 + WHEEL_WIDTH / 4 , height / 2 - WHEEL_HEIGHT / 1.5f);
		Vector2 rightRearWheelPos = new Vector2(width / 2 - WHEEL_WIDTH / 4, height / 2 - WHEEL_HEIGHT / 1.5f);
		Vector2 rightFrontWheelPos = new Vector2(width / 2 - WHEEL_WIDTH / 4, -height / 2 + WHEEL_HEIGHT / 1.5f);
				
		// Define the wheel shape and fixture
		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(WHEEL_WIDTH / 2, WHEEL_HEIGHT / 2);
		FixtureDef wheelFixDef = new FixtureDef();
		wheelFixDef.density = WHEEL_DENSITY;
		wheelFixDef.friction = LINEAR_FRICTION;
		wheelFixDef.shape = wheelShape;
		
		// Create a shape for each wheel
		BodyDef leftFrontWheelBodyDef = new BodyDef();
		leftFrontWheelBodyDef.type = BodyType.DynamicBody;
		leftFrontWheelBodyDef.linearDamping = 1.0f;
		leftFrontWheelBodyDef.angularDamping = 1.0f;
		leftFrontWheelBodyDef.position.set(chassis.getPosition().add(leftFrontWheelPos));
		leftFrontWheel = worldBox2D.createBody(leftFrontWheelBodyDef);
		leftFrontWheel.createFixture(wheelFixDef);
		
		BodyDef rightFrontWheelBodyDef = new BodyDef();
		rightFrontWheelBodyDef.type = BodyType.DynamicBody;
		rightFrontWheelBodyDef.linearDamping = 1.0f;
		rightFrontWheelBodyDef.angularDamping = 1.0f;
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
		leftFrontJointDef.enableLimit = true;
		leftFrontJointDef.maxMotorTorque = 200;
		leftFrontJointDef.lowerAngle = -1 * LOCK_ANGLE;
		leftFrontJointDef.upperAngle = LOCK_ANGLE;
		
		RevoluteJointDef rightFrontJointDef = new RevoluteJointDef();
		rightFrontJointDef.initialize(chassis, rightFrontWheel, rightFrontWheel.getWorldCenter());
		rightFrontJointDef.enableMotor = true;
		rightFrontJointDef.enableLimit = true;
		rightFrontJointDef.maxMotorTorque = 200;
		rightFrontJointDef.lowerAngle = -1 * LOCK_ANGLE;
		rightFrontJointDef.upperAngle = LOCK_ANGLE;
		
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
		
		BodyDef box = new BodyDef();
		box.type = BodyType.StaticBody;
		box.position.set(new Vector2(100, 50));
		
		PolygonShape boxshape = new PolygonShape();
		boxshape.setAsBox(25, 10);
		obs =  worldBox2D.createBody(box);
		obs.createFixture(boxshape, 2);
	}
	
	public void update(float delta) {
		
		// This updates the box2d world
		// Adjust last two params for performance increase
		worldBox2D.step(delta, 8, 8);
		
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
		rotation = chassis.getAngle() * RAD2DEG;
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
		
		// Prevent too much skidding by killing orthogonal
		Vector2 currRightNorm = wheel.getWorldVector( new Vector2(1, 0) );
		Vector2 orhthogAmount = currRightNorm.scl(currRightNorm.dot(wheel.getLinearVelocity()));
		Vector2 impulse = orhthogAmount.scl(wheel.getMass() * -DRIFT_COEFF);
		wheel.applyLinearImpulse(impulse, wheel.getWorldCenter(), true);
		
		// Prevent too much spot - rotation by killing the angular velocity
		float angleImpulse = 0.1f * wheel.getInertia() * DRIFT_COEFF;
		wheel.applyAngularImpulse(wheel.getAngularVelocity() * angleImpulse, true);
		
	}
	
	// These methods are used by the input handler to set engine power and steering angle
	public void powerOnEngine(int direction) {
		enginePower = direction * HORSEPOWER;
	}
	
	public void setSteeringAngle(float steerAmount) {
		steeringAngle = steerAmount * LOCK_ANGLE;
	}
	
	public void powerOffEngine() {
		enginePower /= 1.02;
	}
	
	public void zeroSteeringAngle() {
		steeringAngle = 0;
	}
	
	private void applyForwardForce(Body body, float force) {
		Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1));
		forwardNormal.nor();
		body.applyForce(forwardNormal.scl(force), body.getWorldCenter(), true);
	}
}
