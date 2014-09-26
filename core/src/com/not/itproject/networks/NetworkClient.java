package com.not.itproject.networks;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameVariables;


public class NetworkClient {
	// declare variables
	private static Client client;
	private static Listener clientListener;
	private static Map<Integer, InetAddress> listOfServers;
	
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
			serverAddress = client.discoverHost(GameVariables.VALID_UDP_PORTS[i], 50);
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
						Gdx.app.log("Network", "Received: " + response.message);
					}
					
					// get selection screen info
					else if (object instanceof NetworkMessage.SelectionScreenInformation) {
						NetworkMessage.SelectionScreenInformation info = (NetworkMessage.SelectionScreenInformation) object;
						NetworkHandler.setListOfPlayers(info.playerList);
					}
				} catch (Exception e) {
					// capture errors
				}
			}
		};
	}
	
	// leave current game session
	public void leaveGameSession() {
		// end leave message
		sendConnectionRequest(NetworkMessage.RequestStatus.LEAVE);
		
		// close connection
		client.close();
		
		// reset game session ID
		NetworkHandler.setGameSessionID(-1);
		
		// clear list of players
		NetworkHandler.clearListOfPlayers();
	}
	
	// send messages
	public void sendConnectionRequest(NetworkMessage.RequestStatus type) {
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
