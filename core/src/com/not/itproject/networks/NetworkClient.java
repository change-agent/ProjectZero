package com.not.itproject.networks;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.zero.ProjectZero;


public class NetworkClient {
	// declare variables
	private static Client client;
	private static Listener clientListener;
	private static Map<Integer, InetAddress> listOfServers;
	private static final int DISCOVER_TIMEOUT = 200;
	
	// main constructor
	public NetworkClient() {
		// initialise variables
		listOfServers = new HashMap<Integer, InetAddress>();
	}

	// start client
	public void startClient() {
		// initialise client
		client = new Client();
		client.start();
	}
	
	// search/scan for game sessions
	public void discoverServers() {
		// declare address variable
		InetAddress serverAddress;
		
		// clear list of servers
		clearListOfServers();
		
		// iterate through valid UDP ports
		for (int i=0; i<GameVariables.VALID_UDP_PORTS.length; i++) {
			// try obtain address
			serverAddress = client.discoverHost(GameVariables.VALID_UDP_PORTS[i], DISCOVER_TIMEOUT);
			if (serverAddress != null) { listOfServers.put(i, serverAddress); }
		}
	}
	
	// connect to a visible game session
	public void connectToGameSession(int serverNumber) {
		// connect to server
		try {
			// connect client to server
			client.connect(5000, listOfServers.get(serverNumber), 
					GameVariables.VALID_TCP_PORTS[serverNumber], 
					GameVariables.VALID_UDP_PORTS[serverNumber]);
			
			// register game session ID
			NetworkHandler.setGameSessionID(serverNumber);
		} catch (IOException e) {
			// capture exception
		}
		registerClient();

		// handle incoming connections
		defineClientListener();
		client.addListener(clientListener);
		
		// send join request
		sendConnectionRequest(NetworkMessage.RequestStatus.JOIN);
	}

	// define listener for client
	public static void defineClientListener() {
		clientListener = new Listener() {
			// process received messages
			public void received(Connection connection, Object object) {
				try {
					/* determine object type */
					// connection response
					if (object instanceof NetworkMessage.ConnectionResponse) {
						NetworkMessage.ConnectionResponse response = (NetworkMessage.ConnectionResponse) object;
						if (response.type == NetworkMessage.ResponseStatus.SUCCESS) {
							// proceed with joining
							ProjectZero.roomScreen.enterGameSession();
						} 	
						else if (response.type == NetworkMessage.ResponseStatus.PRIVATE) {
							// disconnect from server
							leaveGameSession();
							
							// display error message
							ProjectZero.errorScreen.displayError(ProjectZero.roomScreen, 
									"Private", "Unable to join private game session.");
						}
						else if (response.type == NetworkMessage.ResponseStatus.FAILURE) {
							// disconnect from server
							leaveGameSession();
							
							// display error message
							ProjectZero.errorScreen.displayError(ProjectZero.roomScreen, 
									"Error", "Unable to join game session.");
						}
					}
					
					// get selection screen info
					else if (object instanceof NetworkMessage.SelectionScreenInformation) {
						NetworkMessage.SelectionScreenInformation info = (NetworkMessage.SelectionScreenInformation) object;
						NetworkHandler.setListOfPlayers(info.playerList);
						NetworkHandler.setListOfPlayerStatus(info.playerStatusList);
					}
					
					// get game start
					else if (object instanceof NetworkMessage.GameStartInformation) {
						NetworkMessage.GameStartInformation info = (NetworkMessage.GameStartInformation) object;
							
						// get player list if exists
						if (info.playerList != null) {
							NetworkHandler.setListOfPlayers(info.playerList);
						}
						
						// proceed to game screen
						ProjectZero.selectionScreen.startGame(); 
					}
					
					// get car information from server
					else if (object instanceof NetworkMessage.GameCarInformation) {
						// get info
						NetworkMessage.GameCarInformation info = (NetworkMessage.GameCarInformation) object;
						
						// process information - update player (other players only)
						if (!AssetHandler.getPlayerID().contains(info.playerID)) {
							// add message to queue
							ProjectZero.gameScreen.getGameWorld().addToNetworkQueue(info);
						}
					}
					
					// get game state information from client
					else if (object instanceof NetworkMessage.GameStateInformation) {
						// get info
						NetworkMessage.GameStateInformation info = (NetworkMessage.GameStateInformation) object;

						// add message to queue (prioritise by clearing)
						ProjectZero.gameScreen.getGameWorld().clearNetworkQueue();
						ProjectZero.gameScreen.getGameWorld().addToNetworkQueue(info);
					}
					
					// get game winner from client
					else if (object instanceof NetworkMessage.GameWinnerInformation) {
						// get info
						NetworkMessage.GameWinnerInformation info = (NetworkMessage.GameWinnerInformation) object;

						// process information - update player (other players only)
						ProjectZero.gameScreen.getGameWorld().addToNetworkQueue(info);
					}
					
				} catch (Exception e) {
					// capture errors
				}
			}
		};
	}
	
	// leave current game session
	public static void leaveGameSession() {
		// end leave message
		sendConnectionRequest(NetworkMessage.RequestStatus.LEAVE);
		
		// close connection
		client.close();
		
		// reset networkHandler and game session
		NetworkHandler.reinitialise();
		ProjectZero.gameSession.newGameSession();
		
		// re-initialise client
		client = new Client();
		client.start();
	}
	
	// send messages
	public static void sendConnectionRequest(NetworkMessage.RequestStatus type) {
		try {
			// send join request
			NetworkMessage.ConnectionRequest request = new NetworkMessage.ConnectionRequest();
			request.playerID = AssetHandler.getPlayerID();
			request.type = type;
			client.sendTCP(request);
		} catch (Exception e) {
			// capture errors
		}
	}
	
	public void sendGameCarInformation(Vector2 position, Vector2 velocity, float rotation) {
		try {
			// send car information
			NetworkMessage.GameCarInformation info = new NetworkMessage.GameCarInformation();
			info.playerID = AssetHandler.getPlayerID();
			info.timestamp = ProjectZero.calendar.getTime();
			info.position = position;
			info.velocity = velocity;
			info.rotation = rotation;
			client.sendTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	public void sendGameStateInformation(NetworkMessage.GameState state) {
		try {
			// send game state information
			NetworkMessage.GameStateInformation info = new NetworkMessage.GameStateInformation();
			info.state = state;
			client.sendTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	public void sendGameWinnerInformation(String playerID) {
		try {
			// send game winner information
			NetworkMessage.GameWinnerInformation info = new NetworkMessage.GameWinnerInformation();
			info.playerID = playerID;
			client.sendTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	public void sendGameSessionInformation(Vector2 lastPosition) {
		try {
			// send game state information
			NetworkMessage.GameSessionInformation info = new NetworkMessage.GameSessionInformation();
			info.playerID = AssetHandler.getPlayerID();
			info.lastPosition = lastPosition;
			client.sendTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	// send (ready state) selection screen
	public void sendSelectionScreenReadyInformation(NetworkMessage.SelectionState state) {
		// send information
		try {
			NetworkMessage.SelectionPlayerStatusInformation info = new NetworkMessage.SelectionPlayerStatusInformation();
			info.playerID = AssetHandler.getPlayerID();
			info.state = state;
			client.sendTCP(info);
		} catch (Exception e) {
			// capture errors
		}
	}

	// register client - messages
	public static void registerClient() {
		Kryo clientKryo = client.getKryo();
		clientKryo.setRegistrationRequired(false);
//		clientKryo.register(NetworkMessage.ConnectionRequest.class);
//		clientKryo.register(NetworkMessage.ConnectionResponse.class);
//		clientKryo.register(NetworkMessage.SelectionScreenInformation.class);
//		clientKryo.register(UUID.class);
	}
	
	// dispose of object
	public void dispose() {
		
	}
	
	/**
	 * @return the listOfServers
	 */
	public Map<Integer, InetAddress> getListOfServers() {
		return listOfServers;
	}
	
	/**
	 * @return clear the listOfServers
	 */
	public void clearListOfServers() {
		listOfServers.clear();
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}
}
