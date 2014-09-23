package com.not.itproject.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
	
	/** ----------------------------- UTILITY OPERATORS ---------------------- **/
	//DEG <-> RAD Conversion functions
	protected static final float DEG_TO_RAD = (float)Math.PI / 180;
	protected static final float RAD_TO_DEG = 1/DEG_TO_RAD;
	protected static final float PPM = 8.0f;
	protected static final float PIXELS_TO_METERS = 1 / PPM;
	protected static final float METERS_TO_PIXELS = PPM; 

	/** ---------------------- START VARIABLES --------------------------- **/
	protected Vector2 position;
	protected float width;
	protected float height;
	protected float rotation;
	protected World worldBox2D;
	protected ObjType objType;
	protected enum ObjType {
		CAR((short)0x0001), 
		OBSTACLE((short)0x0002), 
		POWER((short)0x0004);
		
		public short value;
		ObjType(short value) {this.value = value;}
		public short value() {return value;}
	};
	/** ---------------------- END VARIABLES ----------------------------- **/
	
	/** ---------------------- START CONSTRUCTOR -------------------------- **/
	public GameObject(World worldBox2D, float x, float y, 
			float width, float height, float rotation) 
	{
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.worldBox2D = worldBox2D;
		this.rotation = rotation;
	}
	/** ---------------------- END CONSTRUCTOR --------------------------- **/

	/** ---------------------- POSITION G&S ------------------------------ **/
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	/** ---------------------- END POSITION G&S --------------------------- **/

	/** ------------------------ DIMENSION G&S ---------------------------- **/
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	/** ----------------------- END DIMENSION G&S ------------------------- **/

	/** ---------------------- SPRITE ROTATION G&S ------------------------ **/
	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	/** --------------------- END SPRITE ROTATION G&S --------------------- **/
	
	/** ------------------------ OBJ TYPE G&S ----------------------------- **/
	public ObjType getObjType() {
		return objType;
	}
	/** ------------------------ END OBJ TYPE G&S ------------------------- **/
}
