package com.not.itproject.objects;

import java.util.List;
import java.util.Random;

import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.GameObject.ObjType;
import com.not.itproject.objects.GameVariables.PowerType;

public class PowerUp {
	
	private float lifeTime;
	private PowerType powerType;
	private Random rand;
	
	public PowerUp() {
		this.rand = new Random();
		selectRandomType();
	}
	
	public void printPowerType() {
		if(this.powerType == PowerType.SPEEDBOOST) {
			System.out.print("SpeedBoost");
		}
		if(this.powerType == PowerType.SPEEDREDUCE) {
			System.out.print("SpeedReduce");
		}
		if(this.powerType == PowerType.ICEROAD) {
			System.out.print("Ice road");
		}
		if(this.powerType == PowerType.STARPOWER) {
			System.out.print("star power");
		}
		if(this.powerType == PowerType.STICKYROAD) {
			System.out.print("Sticky road");
		}
	}
	
	// Sets a random power from the enum type
	public void selectRandomType() {
		int enumLength = PowerType.values().length;
		int randIndex = rand.nextInt(enumLength);
		powerType = PowerType.values()[randIndex];
		lifeTime = powerType.getLifeTime();
	}
	
	public PowerType getPowerType() {
		return powerType;
	}

	public boolean hasDepeleted(){
		if(lifeTime <= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updateLifeTime(float delta) {
		this.lifeTime = Math.max(0, lifeTime - delta);
		System.out.println("buff in stack has: " + lifeTime);
	}
	
	// Returns false is the buff we're trying to add doesn't exist, in which case 
	// We should apply the power, prevents multiple applications of pre-existing buffs
	public boolean renewBuff(Player player) {
		int index = player.buffAlreadyActive(this);
		if(index >= 0) {
			player.renewActiveBuff(index, this);
			return true;
		}
		else{
			player.addBuffToStack(this);
			return false;
		}
	}
	
	// Applies power to a specific player
	public void applyPower(Player player, List<Player> opponents) 
	{
		AssetHandler.playSound("usePower");
		if(this.powerType == PowerType.SPEEDBOOST)
		{
			if( renewBuff(player) == false ) {
				player.getCar().setMaxEnginePower(2.0f);
			}	
		}
		if(this.powerType == PowerType.SPEEDREDUCE)
		{
			for (Player opponent : opponents) {
				if(opponent == player) {continue;}
				if( renewBuff(opponent) == false ) {
					opponent.getCar().setMaxEnginePower(0.5f);
				}
			}
		}
		if(this.powerType == PowerType.STARPOWER)
		{
			if( renewBuff(player) == false ) {
				player.getCar().setMaskData((short)~ObjType.OBSTACLE.value());
			}
		}
		if(this.powerType == PowerType.ICEROAD)
		{
			for (Player opponent : opponents) {
				if(opponent == player) {continue;}
				if( renewBuff(opponent) == false ) {
					opponent.getCar().setFriction(0.2f);
				}
			}
		}
		if(this.powerType == PowerType.STICKYROAD)
		{
			for (Player opponent : opponents) {
				if(opponent == player) {continue;}
				if( renewBuff(opponent) == false ) {
					opponent.getCar().setFriction(2.0f);
				}
			}
		}
	}
	
	public void reverseApplyPower(Player player) 
	{
		if(this.powerType == PowerType.SPEEDBOOST)
		{
			player.getCar().setMaxEnginePower(0.5f);
			System.out.println("Power reversed!");
		}
		if(this.powerType == PowerType.SPEEDREDUCE)
		{
			player.getCar().setMaxEnginePower(2.0f);
			System.out.println("Power reversed!");
		}
		if(this.powerType == PowerType.STARPOWER)
		{
			player.getCar().setMaskData((short)0xFFFF);
		}
		if(this.powerType == PowerType.ICEROAD)
		{
			player.getCar().setFriction(1/0.2f);
		}
		if(this.powerType == PowerType.STICKYROAD)
		{
			player.getCar().setFriction(0.5f);
		}
	}
	/** ----------------------------- END METHODS ------------------------- **/
}
