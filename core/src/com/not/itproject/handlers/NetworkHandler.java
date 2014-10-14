package com.not.itproject.handlers;

import java.util.HashMap;
import java.util.Map;

import com.not.itproject.networks.NetworkClient;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.networks.NetworkServer;
import com.not.itproject.screens.SelectionScreen;
import com.not.itproject.screens.SelectionScreen.SelectionState;

public class NetworkHandler {
	// declare variables
	public static NetworkServer server;
	public static NetworkClient client;
	public static int gameSessionID;
	public static boolean gameStart;
	private static Map<Integer, String> listOfPlayers;
	private static Map<String, NetworkMessage.SelectionState> listOfPlayerStatus;

	// main loading function
	public static void load() {
		// define classes
		server = new NetworkServer();
		client = new NetworkClient();
		
		// define session ID
		setGameSessionID(-1);
		setGameStart(false);
		
		// initialise variables
		listOfPlayers = new HashMap<Integer, String>();
		listOfPlayerStatus = new HashMap<String, NetworkMessage.SelectionState>();
		
		clearListOfPlayers();
	}
	
	// reinitialise network handler
	public static void reinitialise() {		
		// redefine session ID
		setGameSessionID(-1);
		setGameStart(false);
		
		// re-initialise variables
		listOfPlayers = new HashMap<Integer, String>();
		listOfPlayerStatus = new HashMap<String, NetworkMessage.SelectionState>();
		
		clearListOfPlayers();
	}
	
	// dispose of network handler
	public static void dispose() {
		server.dispose();
		client.dispose();
	}
	
	/** 
	 * @return whether the user is host or client
	 */
	
	public static boolean isHost() {
		return server.isServerOnline();
	}

	public static boolean isClient() {
		return client.getClient().isConnected();
	}
	
	/**
	 * @return the server
	 */
	public static NetworkServer getNetworkServer() {
		return server;
	}

	/**
	 * @return the client
	 */
	public static NetworkClient getNetworkClient() {
		return client;
	}

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

	/**
	 * @return the gameStart
	 */
	public static boolean getGameStart() {
		return gameStart;
	}

	/**
	 * @param gameStart the gameStart to set
	 */
	public static void setGameStart(boolean gameStart) {
		NetworkHandler.gameStart = gameStart;
	}

	/**
	 * @return the listOfPlayers
	 */
	public static Map<Integer, String> getListOfPlayers() {
		return listOfPlayers;
	}

	/**
	 * @return the listOfPlayerStatus
	 */
	public static Map<String, NetworkMessage.SelectionState> getListOfPlayerStatus() {
		return listOfPlayerStatus;
	}

	/**
	 * @param listOfPlayers the listOfPlayers to set
	 */
	public static void setListOfPlayers(Map<Integer, String> playerList) {
		listOfPlayers = playerList;
	}

	/**
	 * @param listOfPlayerStatus the listOfPlayerStatus to set
	 */
	public static void setListOfPlayerStatus(Map<String, NetworkMessage.SelectionState> playerStatusList) {
		listOfPlayerStatus = playerStatusList;
	}

	/**
	 * @param listOfPlayers the listOfPlayers to clear
	 */
	public static void clearListOfPlayers() {
		listOfPlayers = new HashMap<Integer, String>();
	}

	/**
	 * @param listOfPlayerStatus the listOfPlayerStatus to clear
	 */
	public static void clearListOfPlayerStatus() {
		listOfPlayerStatus = new HashMap<String, NetworkMessage.SelectionState>();
	}
}
