package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.zero.ProjectZero;

public class GameWorld {
	// This is the main game class references through the game world
	public ProjectZero game;
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	public float gameWidth, gameHeight;
	public GameVariables gameVariables;
	public float carWidth;
	public float carHeight;
	
	// Player - the device player
	// List of players called 'opponents' are all other players
	public Player player;
	public List<Player> opponents;
	public List<GameObject> staticObjects;
	
	// Box2D world in which our objects will be created in and handled by
	public World worldBox2D;
	
	// Game state information used to handle screen changes
	public GameState gameStatus;
	public enum GameState { READY, RUNNING, PAUSED, ENDED };

	// main constructor
	public GameWorld(ProjectZero game) {
		// Init the game, game variables and objects
		this.game = game;
		gameVariables = new GameVariables();
		opponents = new ArrayList<Player>();
		staticObjects = new ArrayList<GameObject>();

		// calculate ratio used for object placement
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialize variables
		gameStatus = GameState.RUNNING;
		
		//Initialize box2D world object
		worldBox2D = new World(new Vector2(0.0f, 0.0f), false);

		// define player and opponents
		carWidth = 16;
		carHeight = 32;
		player = new Player(worldBox2D, gameWidth / 2 - gameWidth / 12, 
				gameHeight / 2, carWidth, carHeight, 0);
		//opponents.add(new Player(this, gameWidth / 2 + gameWidth / 12, gameHeight / 2, 24, 40, 0));
	}

	public void update(float delta) {
		// update box2d world
		player.getCar().update(delta);
		
		// update players and check for win
		player.update(delta);
		if (player.getLapNum() == gameVariables.GAMELAPS) {
			gameStatus = GameState.ENDED;
		}
		for (Player opponent : opponents) {
			opponent.update(delta);
			if(opponent.getLapNum() == gameVariables.GAMELAPS) {
				gameStatus = GameState.ENDED;
			}
		}
	}
	
	// Get the player that is playing on the current device
	public Player getPlayer() {
		return player;
	}

	// *** Game state functions ***// 
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

	// Deletes resources after game has been closed
	public void dispose() {
		
	}
}
