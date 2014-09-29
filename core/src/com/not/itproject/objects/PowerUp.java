package com.not.itproject.objects;

import java.util.List;
import com.badlogic.gdx.math.Vector2;

public enum PowerUp {
	
	SPEEDBOOST(5), 
	SPEEDREDUCE(5);
	
	private float duration;
	PowerUp(float duration) {
		this.duration = duration;
	}
	
	public boolean hasDepeleted(){
		if(duration <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updateDuration(float delta) {
		this.duration = Math.max(0, duration - delta);
	}
	
	// Applies power to a specific player
	public void applyPower(Player player, List<Player> opponents) 
	{
		if(this == PowerUp.SPEEDBOOST)
		{
			Vector2 velocity = player.getCar().getVelocity();
			player.getCar().setVelocity(velocity.scl(2.0f));
			System.out.println(velocity.scl(1.2f).x);
			System.out.println(velocity.scl(1.2f).y);
		}
		if(this == PowerUp.SPEEDREDUCE)
		{
			for (Player opponent : opponents) {
				Vector2 velocity = opponent.getCar().getVelocity();
				if(opponent == player) {continue;}
				opponent.getCar().setVelocity(velocity.scl(0.5f));
			}
		}
	}
	
	public void reverseApplyPower(Player player, List<Player> opponents) 
	{
		if(this == PowerUp.SPEEDBOOST)
		{
			Vector2 velocity = player.getCar().getVelocity();
			player.getCar().setVelocity(velocity.scl(0.5f));
			System.out.println(velocity.scl(1.2f).x);
			System.out.println(velocity.scl(1.2f).y);
		}
		if(this == PowerUp.SPEEDREDUCE)
		{
			for (Player opponent : opponents) {
				Vector2 velocity = opponent.getCar().getVelocity();
				if(opponent == player) {continue;}
				opponent.getCar().setVelocity(velocity.scl(2.0f));
			}
		}
	}
	/** ----------------------------- END METHODS ------------------------- **/
}