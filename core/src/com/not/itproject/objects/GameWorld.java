package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.not.itproject.zero.ProjectZero;

public class GameWorld {
	// declare variables
	ProjectZero game;
	
	// Player - the device player
	// List players are all other players
	Player player;
	List<Player> opponents;
	List<GameObject> staticObjects;
	
	float gameWidth, gameHeight;
	GameState gameStatus;
	enum GameState { READY, RUNNING, PAUSED, ENDED };
	static final int GAMELAPS = 3; 
	
	// main constructor
	public GameWorld(ProjectZero game) {
		// define game
		this.game = game;
		opponents = new ArrayList<Player>();
		staticObjects = new ArrayList<GameObject>();

		// calculate ratio
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialise variables
		//gameStatus = GameState.READY;
		gameStatus = GameState.RUNNING;

		// define player and opponents
		player = new Player(gameWidth / 2 - 22, gameHeight / 2, 24, 40, 0);
		opponents.add(new Player(gameWidth / 2 + 22, gameHeight / 2, 24, 40, 0));		
	}

	public void update(float delta) {
		
		// update players and check for win
		player.update(delta);
		if(player.getLapNum() == GAMELAPS) {
			gameStatus = GameState.ENDED;
		}
		for (Player opponent : opponents) {
			opponent.update(delta);
			if(opponent.getLapNum() == GAMELAPS) {
				gameStatus = GameState.ENDED;
			}
		}
		
		//Update static game objects
		
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public GameState getGameState() {
		return gameStatus;
	}
	
	public boolean isReady() {
		if (gameStatus == GameState.READY) {
			return true;
		}
		return false;
	}
	
	public boolean isRunning() {
		if (gameStatus == GameState.RUNNING) {
			return true;
		}
		return false;
	}
	
	public boolean isPaused() {
		if (gameStatus == GameState.PAUSED) {
			return true;
		}
		return false;
	}
	
	public boolean hasEnded() {
		if (gameStatus == GameState.ENDED) {
			return true;
		}
		return false;
	}

	public void dispose() {
		
	}
}
