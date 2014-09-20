package com.not.itproject.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
	// declare variables
	protected Vector2 position;
	protected float width;
	protected float height;
	protected float rotation;
	protected World worldBox2D;
	
	// main constructor
	public GameObject(World worldBox2D, float x, float y, float width, float height, float rotation) {
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.worldBox2D = worldBox2D;
		this.rotation = rotation;
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
}
