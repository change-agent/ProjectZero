package com.not.itproject.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle extends GameObject {

	private Rectangle bounds;

	public Obstacle(GameWorld gameWorld, float x, float y, float width, float height, float rotation) {
		// define super
		super(gameWorld, x, y, width, height, 0);
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void applyEffect(Player player) {
		if(bounds.contains(player.getCar().getBounds())) {
			Vector2 playerVel = player.getCar().getVelocity();
			player.getCar().setVelocity(playerVel.scl(0.8f));
			player.setHit(true);
		}
	}
}
