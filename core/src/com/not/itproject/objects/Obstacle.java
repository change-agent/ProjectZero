package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends GameObject {

	private Rectangle bounds;

	public Obstacle(float x, float y, float width, float height, float rotation) {
		// define super
		super(x, y, width, height, rotation);
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void applyEffect(Player player) {
		if(bounds.contains(player.bounds)) {
			player.velocity = player.velocity.mulAdd(player.velocity, -0.8f);
			player.isHit = true;
		}
	}
}
