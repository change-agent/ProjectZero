package com.not.itproject.networks;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.zero.ProjectZero;


public class NetworkServer {
	// declare variables
	private static Server server;
	private static Listener serverListener;
	private static boolean isServerOnline;
	
	// main constructor
	public NetworkServer() { }
	
	// start a game session
	public void startGameSession() {
		// initialise game session
		server = new Server();
		server.start();

		// connect to server
		int nextPort = 0;
		do {
			try {
				server.bind(GameVariables.VALID_TCP_PORTS[nextPort],
						GameVariables.VALID_UDP_PORTS[nextPort]);
				NetworkHandler.setGameSessionID(nextPort);
				ProjectZero.log("Port - Number: " + nextPort);
				setServerOnline(true);

			} catch (IOException e) {
				// try next port
				setServerOnline(false);
				nextPort += 1;

			}

			// if all ports are taken - return error
			if (nextPort >= GameVariables.VALID_TCP_PORTS.length) {
				// display error message
				ProjectZero.errorScreen.displayError(ProjectZero.roomScreen, 
						"Error", "Unable to create game session." + "\r\n" + 
						"Maximum game sessions reached.");
				break;
			}
		} while (!isServerOnline);
		registerServer();
		
		// add host to list of players
		NetworkHandler.getListOfPlayers().put(
				NetworkHandler.getListOfPlayers().size(), 
				AssetHandler.getPlayerID());
		NetworkHandler.getListOfPlayerStatus().put(
				AssetHandler.getPlayerID(),
				NetworkMessage.SelectionState.PENDING);

		// handle incoming connections
		defineServerListener();
		server.addListener(serverListener);
	}	

	// define listener for server
	public static void defineServerListener() {
		serverListener = new Listener() {
			// process received messages
			public void received(Connection connection, Object object) {
				try {
					/* determine object type */
					// connection request
					if (object instanceof NetworkMessage.ConnectionRequest) {
						// get request
						NetworkMessage.ConnectionRequest request = (NetworkMessage.ConnectionRequest) object;

						switch (request.type) {
							case JOIN:
								// check to see if session has started
								boolean playerAdded = false;
								if (!NetworkHandler.getGameStart()) {
									// add player ID to list of players - if game not started
									for (int i=0; i<GameVariables.MAX_PLAYERS; i++) {
										// check if key exists
										if (!NetworkHandler.getListOfPlayers().containsKey(i)) {
											// add player
											NetworkHandler.getListOfPlayers().put(i, request.playerID);
											NetworkHandler.getListOfPlayerStatus().put(request.playerID, NetworkMessage.SelectionState.PENDING);
											playerAdded = true;
											break;
										}
									}
								}
								
								// check to see if player was added
								if (playerAdded) {
									// send response
									NetworkMessage.ConnectionResponse response = new NetworkMessage.ConnectionResponse();
									response.type = NetworkMessage.ResponseStatus.SUCCESS;
									connection.sendTCP(response);
								} 
								else {
									// send response
									NetworkMessage.ConnectionResponse response = new NetworkMessage.ConnectionResponse();
									response.type = NetworkMessage.ResponseStatus.FAILURE;
									connection.sendTCP(response);
								}
								break;
								
							case LEAVE:
								// remove player ID from list of players
								int key = 0;
								boolean foundKey = false;
								// iterate to find key and remove player
								while (!foundKey) {
									if (NetworkHandler.getListOfPlayers().get(key)
											.contains(request.playerID)) {
										// remove player
										foundKey = true;
										NetworkHandler.getListOfPlayers().remove(
												key);
										NetworkHandler.getListOfPlayerStatus().remove(
												key);
									}
									key++;
									if (key >= NetworkHandler.getListOfPlayers().size()) { foundKey = true; }
								}
								break;
						}
					}
					
					// get car information from client
					else if (object instanceof NetworkMessage.GameCarInformation) {
						// get info
						NetworkMessage.GameCarInformation info = (NetworkMessage.GameCarInformation) object;

						// process information - update player (other players only)
						if (!AssetHandler.getPlayerID().contains(info.playerID)) {
							// add message to queue
							ProjectZero.gameScreen.getGameWorld().addToNetworkQueue(info);
						}
						
						// send information to other clients
						server.sendToAllTCP(info);
					}
					
					// get game state information from client
					else if (object instanceof NetworkMessage.GameStateInformation) {
						// get info
						NetworkMessage.GameStateInformation info = (NetworkMessage.GameStateInformation) object;

						// determine what action to perform
						if (info.state == NetworkMessage.GameState.PAUSE) {							
							// pause game
							ProjectZero.gameScreen.getGameWorld().pauseGame();
						}
						else if (info.state == NetworkMessage.GameState.RESUME) {
							// resume game
							ProjectZero.gameScreen.getGameWorld().resumeGame();
						}
						
						// send information to other clients
						server.sendToAllTCP(info);
					}
					
					// get selection screen player status
					else if (object instanceof NetworkMessage.SelectionPlayerStatusInformation) {
						// get info
						NetworkMessage.SelectionPlayerStatusInformation info = (NetworkMessage.SelectionPlayerStatusInformation) object;
						
						// add state to player
						NetworkHandler.getListOfPlayerStatus().put(info.playerID, info.state);
					}
					
				} catch (Exception e) {
					// capture errors
				}
			}
		};
	}
	
	// end a game session
	public void endGameSession() {
		// close connection
		server.close();
		setServerOnline(false);
				
		// reset networkHandler
		NetworkHandler.reinitialise();
	}

	// send (track/vehicle) selection screen
	public void sendSelectionScreenInformation() {
		// send information
		try {
			NetworkMessage.SelectionScreenInformation info = new NetworkMessage.SelectionScreenInformation();
			info.playerList = NetworkHandler.getListOfPlayers();
			info.playerStatusList = NetworkHandler.getListOfPlayerStatus();
			server.sendToAllTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}
	
	// send game start information
	public void sendGameStart() {
		// send message
		try {
			NetworkMessage.GameStartInformation info = new NetworkMessage.GameStartInformation();
			server.sendToAllTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}
	
	public void sendGameCarInformation(Vector2 position, Vector2 velocity, float rotation) {
		try {
			// send car information
			NetworkMessage.GameCarInformation info = new NetworkMessage.GameCarInformation();
			info.playerID = AssetHandler.getPlayerID();
			Calendar c = new GregorianCalendar();
			info.timestamp = c.getTime();
			info.position = position;
			info.velocity = velocity;
			info.rotation = rotation;
			server.sendToAllTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	public void sendGameStateInformation(NetworkMessage.GameState state) {
		try {
			// send game state information
			NetworkMessage.GameStateInformation info = new NetworkMessage.GameStateInformation();
			info.state = state;
			server.sendToAllTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	// register server - messages
	public static void registerServer() {
		Kryo serverKryo = server.getKryo();
		serverKryo.setRegistrationRequired(false);
//		serverKryo.register(NetworkMessage.ConnectionRequest.class);
//		serverKryo.register(NetworkMessage.ConnectionResponse.class);
//		serverKryo.register(NetworkMessage.SelectionScreenInformation.class);
//		serverKryo.register(UUID.class);
	}
	
	// dispose of object
	public void dispose() {
		
	}
	
	/**
	 * @return the isServerOnline
	 */
	public boolean isServerOnline() {
		return isServerOnline;
	}

	/**
	 * @param isServerOnline the isOnline to set
	 */
	public static void setServerOnline(boolean isServerOnline) {
		NetworkServer.isServerOnline = isServerOnline;
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}
}
