package com.not.itproject.handlers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	private static UUID playerID;
	private static ArrayList<UUID> sessionPlayersID;
	
	// main loading
	public static void load() {
		// initialise variables
		listOfServers = new HashMap<Integer, InetAddress>();
		isConnected = false;
		playerID = UUID.randomUUID();
	}

	public static void startGameSession() {
		// initialise game session
		server = new Server();
		server.start();

		// connect to server
		try {
			server.bind(GameVariables.VALID_TCP_PORTS[0], GameVariables.VALID_UDP_PORTS[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		registerServer();

		// handle incoming connections
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof ConnectionRequest) {
					ConnectionRequest request = (ConnectionRequest) object;
					sessionPlayersID.add(request.uniqueID);

					ConnectionResponse response = new ConnectionResponse();
					response.message = "Connection Established.";
					connection.sendTCP(response);
				}
			}
		});
		
		// set as host
		setHost(true);
		sessionPlayersID = new ArrayList<UUID>();
		sessionPlayersID.add(playerID);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		isConnected = true;
		registerClient();

		// set as host
		setClient(true);
		
		// send request
		ConnectionRequest request = new ConnectionRequest();
		request.message = "Connection Request.";
		request.uniqueID = playerID;
		client.sendTCP(request);
	}
	
	public static void leaveGameSession() {
		// close connection
		client.close();
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

	/**
	 * @return the listOfServers
	 */
	public static Map<Integer, InetAddress> getListOfServers() {
		return listOfServers;
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
		NetworkHandler.isConnected = isConnected;
	}
}
