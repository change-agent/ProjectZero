package com.not.itproject.objects;

public class GameVariables {
	// universal variables
	public final static boolean DEBUG = true;
	
	// networking variables
	public final static int[] VALID_UDP_PORTS = {54777, 54778, 54779};
	public final static int[] VALID_TCP_PORTS = {54555, 54556, 54557};
	public final static int MAX_PLAYERS = 4;
	
	// large range of ports to support more game sessions
	/*
		public final static int[] VALID_UDP_PORTS = {54777, 54778, 54779, 
			54780, 54781, 54782, 54783, 54784, 54785, 54786, 54787, 54788};
		public final static int[] VALID_TCP_PORTS = {54555, 54556, 54557, 
			54558, 54559, 54560, 54561, 54562, 54563, 54564, 54565, 54566};
	*/
	
	// in-game variables
	public enum PlayerColour {RED, BLUE, GREEN, YELLOW};
	public final static int GAME_LAPS = 1;
	public final static float PPM = 8;
	public final static float GRASS_FRICTION_MODIFIER = 1.01f;
	public final static float ROAD_FRICTION_MODIFIER = 1/( 2 *GRASS_FRICTION_MODIFIER );
	
	// Enumerated types for Powerup
	public enum PowerType {
		STARPOWER(5), ICEROAD(4), STICKYROAD(4), SPEEDBOOST(5), SPEEDREDUCE(5);
		
		private float lifeTime;		
		PowerType(float lifeTime) {
			this.lifeTime = lifeTime;
		}
		
		public float getLifeTime() {
			return this.lifeTime;
		}
	};
	

	/** ----------------------------- CAR CONSTANTS ------------------------------ **/
	// Static properties used for car handling
	public static float LOCK_ANGLE = 35 * ((float)Math.PI / 180);
	public static float STEER_SPEED = 0.8f;
	public static float LINEAR_FRICTION = 2.0f;
	public static float CHASSIS_DENSITY = 3.0f;
	public static float WHEEL_DENSITY = 1.8f;
	public static float MAX_HORSEPOWER = 600;
	public static float MIN_HORSEPOWER = 400;
	public static float MIN_DRIFT_COEFF = 0.2f;
	public static float MAX_DRIFT_COEFF = 0.6f;
	public static float DRIFT_COEFF = 0.2f; // Decrease for more skid
}
