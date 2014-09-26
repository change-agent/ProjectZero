package com.not.itproject.networks;

import java.io.IOException;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameVariables;


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
				Gdx.app.log("Port", "Number: " + nextPort);
				setServerOnline(true);

			} catch (IOException e) {
				// try next port
				setServerOnline(false);
				nextPort += 1;

			}

			// if all ports are taken - return error
			if (nextPort >= GameVariables.VALID_TCP_PORTS.length) {
				break;
			}
		} while (!isServerOnline);
		registerServer();
		
		// add host to list of players
		NetworkHandler.getListOfPlayers().put(
				NetworkHandler.getListOfPlayers().size(), 
				AssetHandler.getPlayerID());

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
								// add player ID to list of players
								for (int i=0; i<4 ; i++) {
									// check if key exists
									if (!NetworkHandler.getListOfPlayers().containsKey(i)) {
										// add player
										NetworkHandler.getListOfPlayers().put(i, request.playerID);
										break;
									}
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
									}
									key++;
									if (key >= NetworkHandler.getListOfPlayers().size()) { foundKey = true; }
								}
								break;
						}

						// send response
						NetworkMessage.ConnectionResponse response = new NetworkMessage.ConnectionResponse();
						response.message = "Connection Established.";
						connection.sendTCP(response);
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
		
		// reset game session ID
		NetworkHandler.setGameSessionID(-1);
		
		// clear list of players
		NetworkHandler.clearListOfPlayers();
	}

	// send (track/vehicle) selection screen
	public void sendSelectionScreenInformation() {
		// send information
		try {
			NetworkMessage.SelectionScreenInformation info = new NetworkMessage.SelectionScreenInformation();
			info.playerList = NetworkHandler.getListOfPlayers();
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
