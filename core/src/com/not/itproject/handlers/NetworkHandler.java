package com.not.itproject.handlers;

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
import com.esotericsoftware.kryonet.Server;
import com.not.itproject.networks.ConnectionRequest;
import com.not.itproject.networks.ConnectionResponse;
import com.not.itproject.objects.GameVariables;

public class NetworkHandler {
	// declare variables
	private static Server server;
	private static Client client;
	private static Map<Integer, InetAddress> listOfServers;
	private static boolean isConnected;
	private static boolean isHost;
	private static boolean isClient;
	private static int gameSessionID;
	
	/**
	 * @return the gameSessionID
	 */
	public static int getGameSessionID() {
		return gameSessionID;
	}

	/**
	 * @param gameSessionID the gameSessionID to set
	 */
	public static void setGameSessionID(int gameSessionID) {
		NetworkHandler.gameSessionID = gameSessionID;
	}

	// main loading
	public static void load() {
		// initialise variables
		listOfServers = new HashMap<Integer, InetAddress>();
		isConnected = false;
		setGameSessionID(-1); // reset game session
	}

	public static void startGameSession() {
		// initialise game session
		server = new Server();
		server.start();

		// connect to server
		int nextPort = 0;
		do {
			try {
				server.bind(GameVariables.VALID_TCP_PORTS[nextPort],
						GameVariables.VALID_UDP_PORTS[nextPort]);
				setConnected(true);
				setGameSessionID(nextPort);
				
			} catch (IOException e) {
				// try next port
				setConnected(false);
				nextPort += 1;
				
			}
			
			// if all ports are taken - return error
			if (nextPort >= GameVariables.VALID_TCP_PORTS.length) {
				break;
			}
		} while (!isConnected);
		registerServer();

		// handle incoming connections
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof ConnectionRequest) {
					ConnectionRequest request = (ConnectionRequest) object;
					Gdx.app.log("NETWORK", "Received message: " + request.message);

					ConnectionResponse response = new ConnectionResponse();
					response.message = "Connection Established.";
					connection.sendTCP(response);
				}
			}
		});
		
		// set as host
		setHost(true);
	}
	
	public static void endGameSession() {
		// close connection
		server.close();
	}
	
	public static void setupClient() { 
		// create new client and start
		client = new Client();
		client.start();
	}
	
	public static void discoverServer() {
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

	public static void connectToGameSession(int serverNumber) {
		// connect to server
		try {
			client.connect(5000, listOfServers.get(serverNumber), 
					GameVariables.VALID_TCP_PORTS[serverNumber], 
					GameVariables.VALID_UDP_PORTS[serverNumber]);
			
			setConnected(true);
			setGameSessionID(serverNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		registerClient();

		// set as host
		setClient(true);
	}
	
	public static void leaveGameSession() {
		// close connection
		client.close();
		setConnected(false);
	}
	
	public static void registerServer() {
		Kryo serverKryo = server.getKryo();
		serverKryo.register(ConnectionRequest.class);
		serverKryo.register(ConnectionResponse.class);
		serverKryo.register(UUID.class);
	}
	
	public static void registerClient() {
		Kryo clientKryo = client.getKryo();
		clientKryo.register(ConnectionRequest.class);
		clientKryo.register(ConnectionResponse.class);
		clientKryo.register(UUID.class);
	}

	public static void sendMessage(String text) {	
		// send request
		ConnectionRequest request = new ConnectionRequest();
		request.message = text;
//		request.uniqueID = playerID;
		client.sendTCP(request);
	}
	
	/**
	 * @return the listOfServers
	 */
	public static Map<Integer, InetAddress> getListOfServers() {
		return listOfServers;
	}
	
	/**
	 * @return clear the listOfServers
	 */
	public static void clearListOfServers() {
		listOfServers.clear();
	}

	/**
	 * @return the isConnected
	 */
	public static boolean isConnected() {
		return isConnected;
	}

	/**
	 * @return the isHost
	 */
	public static boolean isHost() {
		return isHost;
	}

	/**
	 * @param isHost the isHost to set
	 */
	public static void setHost(boolean isHost) {
		NetworkHandler.isHost = isHost;
	}

	/**
	 * @return the isClient
	 */
	public static boolean isClient() {
		return isClient;
	}

	/**
	 * @param isClient the isClient to set
	 */
	public static void setClient(boolean isClient) {
		NetworkHandler.isClient = isClient;
	}

	/**
	 * @param isConnected the isConnected to set
	 */
	public static void setConnected(boolean isConnected) {
		setGameSessionID(-1); // reset game session
		NetworkHandler.isConnected = isConnected;
	}
}
