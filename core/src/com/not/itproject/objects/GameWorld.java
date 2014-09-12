package com.not.itproject.objects;

import com.badlogic.gdx.Gdx;
import com.not.itproject.zero.ProjectZero;

public class GameWorld {
	// declare variables
	ProjectZero game;
	Player player;
	float gameWidth, gameHeight;
	GameState gameStatus;
	enum GameState { READY, RUNNING, PAUSED };
	
	// main constructor
	public GameWorld(ProjectZero game) {
		// define game
		this.game = game;

		// calculate ratio
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialise variables
//		gameStatus = GameState.READY;
		gameStatus = GameState.RUNNING;

		// define player
		player = new Player(gameWidth / 2, gameHeight / 2, 24, 40, 0); 
	}

	public void update(float delta) {
		// update player
		player.update(delta);
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

	public void dispose() {
		
	}
}
