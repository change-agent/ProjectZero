package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.objects.GameVariables.PlayerColour;

public class Player {
	private Car car;
	private String playerID;
	private PlayerColour playerColour;
	private int lapNum;
	private List<PowerUp> activeBuffs;
	private HashMap<String, Checkpoint> checkpoints;

	
	// main constructor
	public Player(World worldBox2D, String playerID, PlayerColour colour, float x, float y, 
			float width, float height, float rotation)
	{	
		this.car = new Car(worldBox2D, this, x, y, width, height, 0);
		this.activeBuffs = new ArrayList<PowerUp>(GameVariables.PowerType.values().length);
		this.playerID = playerID;
		this.playerColour = colour;
		this.checkpoints = new HashMap<String, Checkpoint>();
	}
	
	// ---------------- Effect Buffer -----------------------//
	public void update(float delta) {
		car.update(delta);
		Iterator<PowerUp> iter = activeBuffs.iterator();
		while(iter.hasNext()) {
			PowerUp buff = iter.next();
			if(buff.hasDepeleted()) {
				buff.reverseApplyPower(this);
				iter.remove();
			}
			else{
				buff.updateLifeTime(delta);
			}
		}
	}
	
	public int buffAlreadyActive(PowerUp buff) {
		int index = -1;
		for(PowerUp currBuff : activeBuffs) {
			if(currBuff.getPowerType() == buff.getPowerType()) {
				index = activeBuffs.indexOf(currBuff);
			}
		}
		return index;
	}
	
	public void addBuffToStack(PowerUp buff) {
		activeBuffs.add(buff);
	}
	
	public void renewActiveBuff(int index, PowerUp buff) {
		activeBuffs.set(index, buff);
	}
	
	// --------------------- Getter and setters ----------------- //	
	public Car getCar() {
		return car;
	}
	
	public String getPlayerID() {
		return playerID;
	}
	
	public PlayerColour getPlayerColour() {
		return playerColour;
	}
	
	public int getLapNum() {
		return lapNum;
	}

	public void flagUsePower() {
		getCar().setUsePower();
	}
	
	// --------------- Checkpoint handling functions ------------- //

	public Set<String> getCheckpoints() 
	{
		return checkpoints.keySet();
	}
	
	public void addCheckpoint(String name, Checkpoint checkpoint)
	{
		checkpoints.put(name, checkpoint);
	}

	public void clearCheckpoints() {
		checkpoints.clear();		
	}
	
	public void incrementLap() {
		lapNum++;
	}
}