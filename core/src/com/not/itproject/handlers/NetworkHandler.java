package com.not.itproject.handlers;

import java.util.HashMap;
import java.util.Map;

import com.not.itproject.networks.NetworkClient;
import com.not.itproject.networks.NetworkServer;

public class NetworkHandler {
	// declare variables
	public static NetworkServer server;
	public static NetworkClient client;
	public static int gameSessionID;
	private static Map<Integer, String> listOfPlayers;

	// main loading function
	public static void load() {
		// define classes
		server = new NetworkServer();
		client = new NetworkClient();
		
		// define session ID
		setGameSessionID(-1);
		
		// initialise variables
		listOfPlayers = new HashMap<Integer, String>();
		clearListOfPlayers();
	}
	
	// dispose of network handler
	public static void dispose() {
		server.dispose();
		client.dispose();
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
	 * @return the listOfPlayers
	 */
	public static Map<Integer, String> getListOfPlayers() {
		return listOfPlayers;
	}

	/**
	 * @param listOfPlayers the listOfPlayers to set
	 */
	public static void setListOfPlayers(Map<Integer, String> playerList) {
		listOfPlayers = playerList;
	}

	/**
	 * @param listOfPlayers the listOfPlayers to clear
	 */
	public static void clearListOfPlayers() {
		listOfPlayers = new HashMap<Integer, String>();
	}
}
