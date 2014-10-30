package com.not.itproject.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Checkpoint extends GameObject {

	private Body body;
	private String id;
	private float width;
	private float height;
	
	// Creates a static box for the checkpoint and sets it as a sensory object
	// Sensory objects can detect but not action upon collisions
	public Checkpoint(World worldBox2D, float x, float y, float width, float height, float rotation, String id) {
		// define super
		super(worldBox2D, x, y, width, height, 0);
		objType = ObjType.CHECKPOINT;
		this.id = id;
		this.width = width;
		this.height = height;
		
		// Define the box2D body for this power
		BodyDef box = new BodyDef();
		box.type = BodyType.StaticBody;
		box.position.set(new Vector2(x, y));
		
		// Define the box2d shape attached to body
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width/2 * PIXELS_TO_METERS, height/2 * PIXELS_TO_METERS);
		
		// Give the shape a fixture for physics handling
		FixtureDef boxFixDef = new FixtureDef();
		boxFixDef.shape = boxShape;
		boxFixDef.isSensor = true;
		boxFixDef.filter.categoryBits = objType.value();
		
		// Place the object in the world
		body = worldBox2D.createBody(box);
		body.createFixture(boxFixDef);
		body.setUserData(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public Vector2 getPosition() {
		Vector2 centerPos = body.getPosition();
		return centerPos;
	}
}
