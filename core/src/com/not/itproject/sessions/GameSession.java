package com.not.itproject.sessions;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.zero.ProjectZero;

public class GameSession {	
	// declare variables
	private String lastSaved;
	private boolean gameLoaded;
	private ArrayList<GamePlayer> listOfPlayers;
	private String winnerPlayerID;
	private int winnerPlayerIndex;
	
	public GameSession() {
		// initialise variables
		 newGameSession();
	}
	
	public void newGameSession() {
		// initialise variables
		lastSaved = ProjectZero.calendar.getTime().toString();
		listOfPlayers = new ArrayList<GamePlayer>();
		gameLoaded = false;
		winnerPlayerID = "";
		winnerPlayerIndex = 0;
		
		// initialise number of players
		for (int i=0; i<GameVariables.MAX_PLAYERS; i++) {
			listOfPlayers.add(null);
		}
	}
	
	public void startGameSession() {
		// set all players to begin their first lap
		for (GamePlayer player : listOfPlayers) {
			if (player != null) {
				// set to first lap
				player.currentLap = 1;
			}
		}
	}

	public void saveGameSession() {	
		// save game session via assethandler
		lastSaved = ProjectZero.calendar.getTime().toString();
		AssetHandler.saveGameSession();
	}
	
	public void loadGameSession(GameSession session) {
		// load game session
		lastSaved = session.lastSaved;
		listOfPlayers = session.listOfPlayers;
		gameLoaded = true;
	}
	
	public GamePlayer getPlayerByPlayerID(String playerID) {
		// find player by playerID
		for (GamePlayer player : listOfPlayers) {
			if (player != null && player.playerID.contains(playerID)) {
				return player;
			}
		}
		
		// no player with playerID found
		return null;
	}
	
	public void lapCompletedByPlayer(String playerID) {
		// find player by playerID
		for (GamePlayer player : listOfPlayers) {
			if (player != null && player.playerID.contains(playerID)) {
				// increase lap count
				player.currentLap += 1;
			}
		}
	}

	public void setLastPosition(String playerID, float x, float y) {
		// find player by playerID
		for (GamePlayer player : listOfPlayers) {
			if (player != null && player.playerID.contains(playerID)) {
				// set last position
				player.lastPosition = new Vector2(x, y);
			}
		}
	}
	
	/**
	 * @return the listOfPlayers
	 */
	public ArrayList<GamePlayer> getListOfPlayers() {
		return listOfPlayers;
	}
	
	/**
	 * @param listOfPlayers the listOfPlayers to set
	 */
	public void setListOfPlayers(ArrayList<GamePlayer> playerList) {
		listOfPlayers = playerList;
	}

	/**
	 * @param listOfPlayers the listOfPlayers to clear
	 */
	public void clearListOfPlayers() {
		// reinitialise list
		listOfPlayers = new ArrayList<GamePlayer>();
		
		// reinitialise number of players
		for (int i=0; i<GameVariables.MAX_PLAYERS; i++) {
			listOfPlayers.add(null);
		}
	}

	/**
	 * @return the lastSaved
	 */
	public String getLastSaved() {
		return lastSaved;
	}

	/**
	 * @param lastSaved the lastSaved to set
	 */
	public void setLastSaved(String lastSaved) {
		this.lastSaved = lastSaved;
	}

	/**
	 * @return the gameLoaded
	 */
	public boolean isGameLoaded() {
		return gameLoaded;
	}

	/**
	 * @return the winnerPlayerID
	 */
	public String getWinnerPlayerID() {
		return winnerPlayerID;
	}

	/**
	 * @return the winnerPlayerIndex
	 */
	public int getWinnerPlayerIndex() {
		return winnerPlayerIndex;
	}

	/**
	 * @param winnerPlayerID the winnerPlayerID to set
	 */
	public void setWinnerPlayerID(String winnerPlayerID) {
		this.winnerPlayerID = winnerPlayerID;
		this.winnerPlayerIndex = getPlayerByPlayerID(winnerPlayerID).index + 1;
	}
}
