package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.not.itproject.zero.ProjectZero;


public class ToggleButton extends SimpleButton {
	// declare variables
	boolean isToggleOn;
	
	public ToggleButton(int x, int y, int width, int height, boolean toggled) {
		// define super
		super(x, y, width, height);
		
		// initialise variables
		isToggleOn = toggled;
	}
	

	@Override
	public void update(float delta) {
		// super update
		super.update(delta);
		
		// check if pressed
		if (isTouched) {
			isToggleOn = !isToggleOn;
		}
	}

	/**
	 * @return the isToggled
	 */
	public boolean isToggleOn() {
		return isToggleOn;
	}

	/**
	 * @param isToggled the isToggled to set
	 */
	public void setToggleOn(boolean isToggleOn) {
		this.isToggleOn = isToggleOn;
	}
	
}
