package com.not.itproject.sessions;

import com.badlogic.gdx.math.Vector2;

public class GamePlayer {
	/* player index */
	public int index;
	
	/* player related variables */
	public String playerID;
	public int currentLap;
	
	/* player last position/rotation */
	public Vector2 lastPosition;
	public float lastRotation;
}