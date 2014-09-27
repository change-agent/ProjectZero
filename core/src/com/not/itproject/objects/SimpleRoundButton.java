package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.zero.ProjectZero;

public class SimpleRoundButton {
	// declare variables
	private Vector2 position;
	private int radius;
	private Circle bounds;
	protected boolean isTouched;
	
	public SimpleRoundButton(int x, int y, int radius) {
		// initialise variables
		position = new Vector2(x, y);
		this.radius = radius;
		bounds = new Circle(x, y, radius);
		isTouched = false;
	}

	public void update(float delta) {
		// check if pressed
		isTouched = pressed((int) (Gdx.input.getX() * ProjectZero.RATIO), 
				(int) (Gdx.input.getY() * ProjectZero.RATIO));
	}
	
	public boolean pressed(int x, int y) {
		// determine if pressed
		if (Gdx.input.justTouched()) {
			// check if within bounds
			if(bounds.contains(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	public Circle getBounds() {
		return bounds;
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
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param width the radius to set
	 */
	public void setWidth(int radius) {
		this.radius = radius;
	}

	/**
	 * @return the isTouched
	 */
	public boolean isTouched() {
		return isTouched;
	}
}
