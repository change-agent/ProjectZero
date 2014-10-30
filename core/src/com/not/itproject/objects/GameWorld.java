
package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.handlers.TiledMapHandler;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.objects.GameObject.ObjType;
import com.not.itproject.objects.GameVariables.PlayerColour;
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
	
	// Network handling
	private Queue<Object> networkUpdates;
	
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
			
			if (getPlayer().getCar().equals((Car) B.getBody().getUserData()) ||
					getPlayer().getCar().equals((Car) B.getBody().getUserData())) {
				if (((GameObject) A.getBody().getUserData()).getObjType() 
						!= GameObject.ObjType.CHECKPOINT || 
					((GameObject) B.getBody().getUserData()).getObjType() 
						!= GameObject.ObjType.CHECKPOINT) {
					// tactile feedback
					AssetHandler.vibrate(50);
				}
			}
			
			if(A.isSensor()) 
			{
				GameObject gameObject = (GameObject) A.getBody().getUserData();
				Car car = (Car) B.getBody().getUserData();
				if(gameObject.getObjType() == GameObject.ObjType.CHECKPOINT)
				{
					// Handle the checkpoint detection
					Checkpoint checkpoint = (Checkpoint)gameObject;
					
					// record the players last location
					if (!car.getOwner().getCheckpoints().contains(checkpoint.getId())) {
						// obtain if checkpoint not collected
						ProjectZero.log("Checkpoint Obtained!");
						Vector2 lastPosition = new Vector2(car.getPosition().x, car.getPosition().y);
						if (NetworkHandler.isHost()) {
							// update game session as host
							ProjectZero.gameSession.setLastPosition(AssetHandler.getPlayerID(), 
									lastPosition.x, lastPosition.y);
						}
						else
						{
							// send last position data to host
							NetworkHandler.getNetworkClient().sendGameSessionInformation(lastPosition);
						}
					}
					car.getOwner().addCheckpoint(checkpoint.getId(), checkpoint);
					
					// check if player has completed a lap
					if(car.getOwner().getCheckpoints().size() == checkpoints.size()
							&& checkpoint.getId().compareToIgnoreCase("1") == 0)
					{
						car.getOwner().clearCheckpoints();
						car.getOwner().incrementLap();
					}
				}
				else
				{	
					// Handle the power up collection
					if(car.getOwner().getPlayerID() == getPlayer().getPlayerID())
					{
						AssetHandler.playSound("powerUpCollected");
					}
					PowerUpContainer powerUpContainer = (PowerUpContainer) gameObject;
					car.setPower(powerUpContainer.getPowerUp());
					powerUpContainer.CollectPowerUp();
				}
			}
			
			else if(B.isSensor()) 
			{
				GameObject gameObject = (GameObject) B.getBody().getUserData();
				Car car = (Car) A.getBody().getUserData();
				if(gameObject.getObjType() == GameObject.ObjType.CHECKPOINT)
				{
					// Handle the checkpoint detection
					Checkpoint checkpoint = (Checkpoint)gameObject;
					
					// record the players last location
					if (!car.getOwner().getCheckpoints().contains(checkpoint.getId())) {
						// obtain if checkpoint not collected
						ProjectZero.log("Checkpoint Obtained!");
						ProjectZero.gameSession.setLastPosition(AssetHandler.getPlayerID(), 
								car.getPosition().x, car.getPosition().y);
					}
					car.getOwner().addCheckpoint(checkpoint.getId(), checkpoint);
					
					// check if player has completed a lap
					if(car.getOwner().getCheckpoints().size() == checkpoints.size() 
							&& checkpoint.getId().compareToIgnoreCase("1") == 0)
					{
						car.getOwner().clearCheckpoints();
						car.getOwner().incrementLap();
					}
				}
				else
				{	
					PowerUpContainer powerUpContainer = (PowerUpContainer) gameObject;
					car.setPower(powerUpContainer.getPowerUp());
					powerUpContainer.CollectPowerUp();
					// Handle the power up collection
					if(car.getOwner().getPlayerID() == getPlayer().getPlayerID())
					{
						AssetHandler.playSound("powerUpCollected");
					}
				}
			}
			else 
			{	
				// TODO => this will pay sounds for all players
				AssetHandler.playSound("crash");
			}
		}
	};
	/**-------------------------------- END -------------------------------**/
	
	// Player - the device player
	// List of players called 'opponents' are all other players
	public List<Player> players;
	public List<GameObject> staticObjects;
	public List<Checkpoint> checkpoints;
	public TiledMapHandler tiledMapHandler;
	
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
		checkpoints = new ArrayList<Checkpoint>();

		// calculate ratio used for object placement
		gameWidth = ProjectZero.GAME_WIDTH;
		gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// initialize variables
		gameStatus = GameState.READY;
		
		//Initialize box2D world object
		worldBox2D = new World(new Vector2(0.0f, 0.0f), false);
		worldBox2D.setContactListener(c);

		// define player and opponents
		carWidth = 16;
		carHeight = 32;
		
		// Set up the map
		tiledMapHandler = new TiledMapHandler(this, 0);
		
		// define network update queue
		networkUpdates = new ConcurrentLinkedQueue<Object>();
	}

	// create objects into the world
	public void initialiseGameWorld() {
		// This worlds player will always be at index 0
		// This makes using powers more transparent and will allow for network data efficiency
		PlayerColour colour = null;
		MapObjects startingPositions = tiledMapHandler.getStartingPositions();
		for (int i=0; i<GameVariables.MAX_PLAYERS; i++) {
			switch (i) {
				case 0:
					// first player is red
					colour = GameVariables.PlayerColour.RED;
					break;
				case 1:
					// second player is blue
					colour = GameVariables.PlayerColour.BLUE;
					break;
				case 2:
					// third player is blue
					colour = GameVariables.PlayerColour.GREEN;
					break;
				case 3:
					// fourth player is blue
					colour = GameVariables.PlayerColour.YELLOW;
					break;
			}
			
			// check if player exists
			if (NetworkHandler.getListOfPlayers().get(i) != null) {
				
				// load player data
				String currPlayerID = NetworkHandler.getListOfPlayers().get(i).playerID;
				if (NetworkHandler.getListOfPlayers().get(i).lastPosition.x == 0
						&& NetworkHandler.getListOfPlayers().get(i).lastPosition.y == 0) {
					// Access the tiled map to place them at the correct position
					if(i < startingPositions.getCount())
					{
						RectangleMapObject startPos = (RectangleMapObject)startingPositions.get(i);
						float x = startPos.getRectangle().x;
						float y = startPos.getRectangle().y;
						players.add(new Player(worldBox2D, currPlayerID, colour,
								x, y, carWidth, carHeight, 0, 
								tiledMapHandler.getMapWidth(),
								tiledMapHandler.getMapHeight()));
					}
				} else {
					// load values
					players.add(new Player(worldBox2D, currPlayerID, colour,
							ProjectZero.gameSession
									.getPlayerByPlayerID(currPlayerID).lastPosition.x,
							ProjectZero.gameSession
									.getPlayerByPlayerID(currPlayerID).lastPosition.y,
							carWidth, carHeight, 0,
							tiledMapHandler.getMapWidth(),
							tiledMapHandler.getMapHeight()));
				}
			}
		}
	}

	public void update(float delta) {

		// This updates the box2d world
		// Adjust last two params for performance increase
		worldBox2D.step(delta, 4, 4);

		// process network updates to box2d world
		while (networkUpdates.size() != 0) {
			// go through queues
			Object object = networkUpdates.remove();

			// player information
			if (object instanceof NetworkMessage.GameCarInformation) {
				// get info
				NetworkMessage.GameCarInformation info = (NetworkMessage.GameCarInformation) object;

				// update players according
				ProjectZero.gameScreen.updatePlayer(info.playerID,
						info.position, info.velocity, info.rotation);
			}

			// game state information
			else if (object instanceof NetworkMessage.GameStateInformation) {
				// get info
				NetworkMessage.GameStateInformation info = (NetworkMessage.GameStateInformation) object;

				// determine what action to perform
				if (info.state == NetworkMessage.GameState.PAUSE) {
					// pause game
					ProjectZero.gameScreen.getGameWorld().pauseGame();
				} else if (info.state == NetworkMessage.GameState.RESUME) {
					// resume game
					ProjectZero.gameScreen.getGameWorld().resumeGame();
				} else if (info.state == NetworkMessage.GameState.EXIT) {
					// check if host
					if (NetworkHandler.isHost()) {
						// save game
						ProjectZero.gameScreen.getGameWorld().saveGame();
					}

					// exit game
					ProjectZero.gameScreen.getGameWorld().endGame();
					return;
				}
			}
			
			// game winner information
			else if (object instanceof NetworkMessage.GameWinnerInformation) {
				// get info - winner
				NetworkMessage.GameWinnerInformation info = (NetworkMessage.GameWinnerInformation) object;
				ProjectZero.gameSession.setWinnerPlayerID(info.playerID);
				
				// end game
				endingGame();
			}
		}
		
		// check for win - individual
		if (getPlayerByID(AssetHandler.getPlayerID()).getLapNum() == GameVariables.GAME_LAPS) {
			// enter end state
			if (NetworkHandler.isHost()) {
				// set winner
				ProjectZero.gameSession.setWinnerPlayerID(AssetHandler.getPlayerID());

				// end game as host
				endingGame();
				
				// send winner to players
				NetworkHandler.getNetworkServer().sendGameWinnerInformation(AssetHandler.getPlayerID());
			} 
			else if (NetworkHandler.isClient()) {
				// send winner to players
				NetworkHandler.getNetworkClient().sendGameWinnerInformation(AssetHandler.getPlayerID());
			}
		}
		
		// update players 
		for (Player player : players) {
			player.update(delta);
			float friction = tiledMapHandler.getFrictionFromPosition(player.getCar().getPosition());
			player.getCar().setFriction(friction);
			
			// Use powers if necessary
			if(player.getCar().usePower()){
				PowerUp power = player.getCar().getPower();
				player.getCar().removePower();
				if(power != null)
				{
					power.applyPower(player, players);
				}
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
	
	// ----------------- Tiled map getters and setters --------- //
	public TiledMap getTiledMap() {
		return tiledMapHandler.getTiledMap();
	}
	
	public int getMapHeight() {
		return tiledMapHandler.getMapHeight();
	}
	
	public int getMapWidth() {
		return tiledMapHandler.getMapWidth();
	}
	
	// ----------------- Network functions ---------------------//
	public Player getPlayerByID(String playerID) {
		for (int i=0; i<players.size(); i++) {
			if (players.get(i).getPlayerID().contains(playerID)) {
				return players.get(i);
			}
		}
		return null;
	}
	
	// functions for network update queue
	public void addToNetworkQueue(Object obj) {
		// add to queue
		networkUpdates.add(obj);
	}
	
	// function to clear network queue
	public void clearNetworkQueue() {
		// clear queue
		networkUpdates = new ConcurrentLinkedQueue<Object>();
	}


	// ------------ Gamestate functions ----------------------//
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

		// send network message to players
	}
	
	public void resumeGame() {
		// stop movement of car
		ProjectZero.gameScreen.getGameWorld().getPlayer().getCar().powerOffEngine(true);
		ProjectZero.gameScreen.getGameWorld().getPlayer().getCar().zeroSteeringAngle();
		
		// game resume - countdown
		gameStatus = GameState.READY;
	}
	
	public void pauseGame() {
		// stop movement of car
		ProjectZero.gameScreen.getGameWorld().getPlayer().getCar().powerOffEngine(true);
		ProjectZero.gameScreen.getGameWorld().getPlayer().getCar().zeroSteeringAngle();
		
		// game paused
		gameStatus = GameState.PAUSED;
	}

	public void endingGame() {
		// game ending
		gameStatus = GameState.ENDED;
	}
	
	public void endGame() {
		// reinitialise ProjectZero
		game.initialise();
		
		// return to menu screen
		game.nextScreen(ProjectZero.mainScreen, null);
		
		// disconnect from server
		if (NetworkHandler.isHost()) {
			// as server
			NetworkHandler.getNetworkServer().endGameSession();
		} else if (NetworkHandler.isClient()) {
			// as client
			NetworkHandler.getNetworkClient().leaveGameSession();
		}
	}
	
	public void saveGame() {
		// game - save
		ProjectZero.log("Saving game.");
		ProjectZero.gameSession.saveGameSession();
		ProjectZero.log("Game saved.");
	}
	
	// Deletes resources after game has been closed
	public void dispose() {
		
	}
	
	
}
