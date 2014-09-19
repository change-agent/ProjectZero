package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.zero.ProjectZero;

public class GameWorld {
	// declare variables
	ProjectZero game;
	
	// Player - the device player
	// List players are all other players
	public Player player;
	public List<Player> opponents;
	public List<GameObject> staticObjects;
	
	// Box2D Variables
	public World worldBox2D;
	public final Vector2 gravity = new Vector2(0, 0);//new Vector2(0, 9.8f);
	
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	float gameWidth, gameHeight;
	GameState gameStatus;
	enum GameState { READY, RUNNING, PAUSED, ENDED };
	static final int GAMELAPS = 3;
	public static final float FRICTION = 2.0f; 
	
	// main constructor
	public GameWorld(ProjectZero game) {
		// define game
		this.game = game;
		opponents = new ArrayList<Player>();
		staticObjects = new ArrayList<GameObject>();

		// calculate ratio used for object placement
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialize variables
		gameStatus = GameState.RUNNING;
		
		//Initialize box2D world object
		worldBox2D = new World(gravity, false);

		// define player and opponents
		player = new Player(this, gameWidth / 2 - gameWidth / 12, gameHeight / 2, 24, 40, 0);
		//opponents.add(new Player(this, gameWidth / 2 + gameWidth / 12, gameHeight / 2, 24, 40, 0));
	}

	public void update(float delta) {
		// update box2d world
		worldBox2D.step(delta, 1, 1);
		
		// update players and check for win
		player.update(delta);
		if (player.getLapNum() == GAMELAPS) {
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
	
	public void resumeGame() {
		// game resume - countdown
		gameStatus = GameState.READY;
	}
	
	public void pauseGame() {
		// game paused
		gameStatus = GameState.PAUSED;
	}

	public void dispose() {
		
	}
}
