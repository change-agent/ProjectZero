package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends GameObject {

	private Rectangle bounds;

	public Obstacle(World worldBox2D, float x, float y, float width, float height, float rotation) {
		// define super
		super(worldBox2D, x, y, width, height, 0);
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void applyEffect(Player player) {
		if(bounds.contains(player.getCar().getBounds())) {
			
		}
	}
}
