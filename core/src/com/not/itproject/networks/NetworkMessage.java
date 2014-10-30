package com.not.itproject.networks;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.not.itproject.sessions.GamePlayer;

public class NetworkMessage {
	public enum RequestStatus {JOIN, LEAVE};
	public enum ResponseStatus {SUCCESS, PRIVATE, FAILURE};
	public enum GameState {RESUME, PAUSE, EXIT};
	public enum SelectionState {PENDING, READY, WAITING};
	
	public static class ConnectionRequest {
		public String playerID;
		public Date timeStamp;
		public RequestStatus type;
	}
	
	public static class ConnectionResponse {
		public ResponseStatus type;
	}
	
	public static class SelectionScreenInformation {
		public ArrayList<GamePlayer> playerList;
		public Map<String, SelectionState> playerStatusList;
	}
	
	public static class SelectionPlayerStatusInformation {
		public String playerID;
		public SelectionState state;
	}
	
	public static class GameStartInformation {
		public ArrayList<GamePlayer> playerList;
	}
	
	public static class GameCarInformation {
		public String playerID;
		public Date timestamp;
		public Vector2 position;
		public Vector2 velocity;
		public float rotation;
	}
	
	public static class GameStateInformation {
		public GameState state;
	}
	
	public static class GameSessionInformation {
		public String playerID;
		public Vector2 lastPosition;
	}
	
	public static class GameWinnerInformation {
		public String playerID;
	}
}
