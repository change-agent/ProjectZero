package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameObject.ObjType;
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
	
	
	/**------------------- DEBUG CONTACT LISTENER FOR POWER UP TESTING------**/
	public ContactListener c = new ContactListener() {
		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {}
		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {}
		@Override
		public void endContact(Contact contact) {}
		
		@Override
		public void beginContact(Contact contact) {
			Fixture A = contact.getFixtureA();
			Fixture B = contact.getFixtureB();
			
			if(A.isSensor()) {
				System.out.println("power coolected");
				PowerUpContainer powerUpContainer = (PowerUpContainer) A.getBody().getUserData();
				Car car = (Car) B.getBody().getUserData();
				car.setPower(powerUpContainer.getPowerUp());
				powerUpContainer.CollectPowerUp();
			}
			
			if(B.isSensor()) {
				System.out.println("power coolected");
				PowerUpContainer powerUpContainer = (PowerUpContainer) B.getBody().getUserData();
				Car car = (Car) A.getBody().getUserData();
				car.setPower(powerUpContainer.getPowerUp());
				powerUpContainer.CollectPowerUp();
			}
		}
	};
	/**-------------------------------- END -------------------------------**/
	
	// Player - the device player
	// List of players called 'opponents' are all other players
	public List<Player> players;
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
		players = new ArrayList<Player>();
		staticObjects = new ArrayList<GameObject>();

		// calculate ratio used for object placement
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialize variables
//		gameStatus = GameState.READY;
		gameStatus = GameState.RUNNING;
		
		//Initialize box2D world object
		worldBox2D = new World(new Vector2(0.0f, 0.0f), false);
		worldBox2D.setContactListener(c);

		// define player and opponents
		carWidth = 16;
		carHeight = 32;
	}

	// create objects into the world
	public void initialiseGameWorld() {
		// This worlds player will always be at index 0
		// This makes using powers more transparent and will allow for network data efficiency
		for (int i=0; i<NetworkHandler.getListOfPlayers().size(); i++) {
			players.add(new Player(worldBox2D, NetworkHandler.getListOfPlayers().get(i), 
					gameWidth / 2 + i*carWidth*2, gameHeight / 2, carWidth, carHeight, 0));
		}
	}

	public void update(float delta) {
		
		// This updates the box2d world
		// Adjust last two params for performance increase
		worldBox2D.step(delta, 4, 4);
		
		// update players and check for win
		for (Player player : players) {
			player.update(delta);
			
			if(player.getLapNum() == gameVariables.GAMELAPS) {
				gameStatus = GameState.ENDED;
			}
			
			// Use powers if necessary
			if(player.getCar().usePower()){
				PowerUp power = player.getCar().getPower();
				player.getCar().removePower();
				power.applyPower(player, players);
			}
		}
		
		for (GameObject staticObj : staticObjects) {
			if(staticObj.getObjType() == ObjType.POWER_UP_CONTAINER){
				PowerUpContainer powerUpContainer = (PowerUpContainer) staticObj;
				powerUpContainer.update(delta);
			}
		}
	}
	
	// Get the player that is playing on the current device
	public Player getPlayer() {
		return getPlayerByID(AssetHandler.getPlayerID());
	}
	
	public Player getPlayerByID(String playerID) {
		for (int i=0; i<players.size(); i++) {
			if (players.get(i).getPlayerID().contains(playerID)) {
				return players.get(i);
			}
		}
		return null;
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
	
	public void startGame() {
		// game start
		gameStatus = GameState.RUNNING;
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
