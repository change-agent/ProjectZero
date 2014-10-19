package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.zero.ProjectZero;

public class SimpleButton {
	// declare variables
	private Vector2 position;
	private int width;
	private int height;
	private Rectangle bounds;
	protected boolean isTouched;
	
	public SimpleButton(int x, int y, int width, int height) {
		// initialise variables
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		bounds = new Rectangle(x, y, width, height);
		isTouched = false;
	}

	public void update(float delta) {
		// check if pressed
		isTouched = pressed((int) (Gdx.input.getX() * ProjectZero.RATIO), 
				(int) (Gdx.input.getY() * ProjectZero.RATIO));
		if(isTouched)
		{
			AssetHandler.playSound("buttonClick");
		}
	}
	
	public boolean pressed(int x, int y) {
		// determine if pressed
		if (Gdx.input.justTouched()) {
			// check if within bounds
			if (bounds.contains(x, y)) {
//				(x >= position.x && x <= (position.x + width) && y >= position.y && y <= (position.y + height))
				return true;
			}
		}
		return false;
	}
	
	public Rectangle getBounds() {
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
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the isTouched
	 */
	public boolean isTouched() {
		return isTouched;
	}
}
