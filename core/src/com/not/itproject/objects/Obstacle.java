package com.not.itproject.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Intersector;

public class Obstacle extends GameObject {
	private Rectangle bounds;

	public void MapObstacle(Player player) {
		// Select the layer that contains the invisible collision tiles
		int objectLayerId = 2;
			
		// Extract all objects from the collision layer
		TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)GameRenderer.tiledMap.getLayers().get(objectLayerId);
		MapObjects objects = collisionObjectLayer.getObjects();
		
		// Check each of those objects for a collision
		for(RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
			Rectangle rectangle = rectangleObject.getRectangle();
			
		// Updated Player code has broken this method of checking for collisions – included only for reference
		/*if(Intersector.overlaps(rectangle, player.getCar().getBounds())) {
		    // collision logic here
		  }*/
		}
	}

	private Body body;
	
	public Obstacle(World worldBox2D, float x, float y, float width, float height, float rotation) {
		// define super
		super(worldBox2D, x, y, width, height, 0);
		objType = ObjType.OBSTACLE;
		
		// Define the box2D body for this power
		BodyDef box = new BodyDef();
		box.type = BodyType.StaticBody;
		box.position.set(new Vector2(x, y));
		
		// Define the box2d shape attached to body
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width/2, height/2);
		
		// Give the shape a fixture for physics handling
		FixtureDef boxFixDef = new FixtureDef();
		boxFixDef.density = 5.0f;
		boxFixDef.shape = boxShape;
		boxFixDef.restitution = 0.8f;
		boxFixDef.filter.categoryBits = objType.value();
		
		// Place the object in the world
		body = worldBox2D.createBody(box);
		body.createFixture(boxFixDef);
		body.setUserData(this);
	}
}
