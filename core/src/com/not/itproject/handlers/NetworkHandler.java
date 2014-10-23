package com.not.itproject.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.not.itproject.networks.NetworkClient;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.networks.NetworkServer;
import com.not.itproject.sessions.GamePlayer;
import com.not.itproject.zero.ProjectZero;

	
public class NetworkHandler {
	// declare variables
	public static NetworkServer server;
	public static NetworkClient client;
	public static int gameSessionID;
	public static boolean gameStart;
	private static Map<String, NetworkMessage.SelectionState> listOfPlayerStatus;

	// main loading function
	public static void load() {
		// define classes
		server = new NetworkServer();
		client = new NetworkClient();
		
		// define session ID
		setGameSessionID(-1);
		setGameStart(false);
		
		clearListOfPlayers();
		clearlistOfPlayerStatus();
	}
	
	// reinitialise network handler
	public static void reinitialise() {		
		// redefine session ID
		setGameSessionID(-1);
		setGameStart(false);
		
		// re-initialise variables
		clearListOfPlayers();
		clearlistOfPlayerStatus();
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
	public static ArrayList<GamePlayer> getListOfPlayers() {
		return ProjectZero.gameSession.getListOfPlayers();
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
	public static void setListOfPlayers(ArrayList<GamePlayer> playerList) {
		ProjectZero.gameSession.setListOfPlayers(playerList);
	}

	/**
	 * @param listOfPlayerStatus the listOfPlayerStatus to set
	 */
	public static void setListOfPlayerStatus(Map<String, NetworkMessage.SelectionState> playerStatusList) {
		listOfPlayerStatus = playerStatusList;
	}

	/**
	 * @param listOfPlayerStatus the listOfPlayerStatus to clear
	 */
	public static void clearlistOfPlayerStatus() {
		listOfPlayerStatus = new HashMap<String, NetworkMessage.SelectionState>();
	}
	
	/**
	 * @param listOfPlayers the listOfPlayers to clear
	 */
	public static void clearListOfPlayers() {
		ProjectZero.gameSession.clearListOfPlayers();
	}

	/**
	 * @param listOfPlayerStatus the listOfPlayerStatus to clear
	 */
	public static void clearListOfPlayerStatus() {
		listOfPlayerStatus = new HashMap<String, NetworkMessage.SelectionState>();
	}
}
