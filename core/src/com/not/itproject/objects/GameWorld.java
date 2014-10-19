package com.not.itproject.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
	private Queue<NetworkMessage.GameCarInformation> networkUpdates;
	
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
				// tactile feedback
				AssetHandler.vibrate(50);
			}
			
			if(A.isSensor()) 
			{
				GameObject gameObject = (GameObject) A.getBody().getUserData();
				Car car = (Car) B.getBody().getUserData();
				if(gameObject.getObjType() == GameObject.ObjType.CHECKPOINT)
				{
					// Handle the checkpoint detection
					Checkpoint checkpoint = (Checkpoint)gameObject;
					car.getOwner().addCheckpoint(checkpoint.getId(), checkpoint);
					if(car.getOwner().getCheckpoints().size() == checkpoints.size()
							&& checkpoint.getId().compareToIgnoreCase("1") == 0)
					{
						car.getOwner().clearCheckpoints();
						car.getOwner().incrementLap();
						System.out.println(car.getOwner().getLapNum());
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
					car.getOwner().addCheckpoint(checkpoint.getId(), checkpoint);
					if(car.getOwner().getCheckpoints().size() == checkpoints.size() 
							&& checkpoint.getId().compareToIgnoreCase("1") == 0)
					{
						car.getOwner().clearCheckpoints();
						car.getOwner().incrementLap();
						System.out.println(car.getOwner().getLapNum());
					}
				}
				else
				{	
					// Handle the power up collection
					PowerUpContainer powerUpContainer = (PowerUpContainer) gameObject;
					car.setPower(powerUpContainer.getPowerUp());
					powerUpContainer.CollectPowerUp();
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
	TiledMapHandler tiledMapHandler;
	
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
		networkUpdates = new ConcurrentLinkedQueue<NetworkMessage.GameCarInformation>();
	}

	// create objects into the world
	public void initialiseGameWorld() {
		// This worlds player will always be at index 0
		// This makes using powers more transparent and will allow for network data efficiency
		PlayerColour colour = null;
		for (int i=0; i<NetworkHandler.getListOfPlayers().size(); i++) {
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
			players.add(new Player(worldBox2D, NetworkHandler.getListOfPlayers().get(i), colour,
					gameWidth / 2 + i*carWidth*2, gameHeight / 2, carWidth, carHeight, 0));
		}
	}

	public void update(float delta) {
		
		// This updates the box2d world
		// Adjust last two params for performance increase
		worldBox2D.step(delta, 4, 4);
		
		// process network updates to box2d world
		while (networkUpdates.size() != 0) {
			// go through queues
			NetworkMessage.GameCarInformation info = networkUpdates.remove();
			
			// add a check for threshold of timestamp (time difference)
//			Calendar c = new GregorianCalendar();
//			ProjectZero.log("Delay - " + (((Calendar)(c.getTime()).get(Calendar.SECOND) - info.timestamp.get(Calendar.SECOND)) / 1000));
//			ProjectZero.log("Current Time: " + c.getTime());
//			ProjectZero.log("Message Time: " + info.timestamp);
			
			// update players according
			ProjectZero.gameScreen.updatePlayer(info.playerID,
					info.position, info.velocity, info.rotation);
		}
		
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
	public void addToNetworkQueue(NetworkMessage.GameCarInformation obj) {
		// add to queue
		networkUpdates.add(obj);
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

	public void endGame() {
		// game end
		//// TO-DO RESET ALL VARIABLES
	}
	
	public void loadGame(/* VALUES TO LOAD */) {
		// game - load
		//// TO-DO LOAD ALL VARIABLES
	}
	
	public void saveGame() {
		// game - save
		//// TO-DO SAVE ALL VARIABLES
	}
	
	// Deletes resources after game has been closed
	public void dispose() {
		
	}
	
	
}
