package com.not.itproject.networks;

import java.util.Date;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.not.itproject.screens.SelectionScreen.SelectionState;

public class NetworkMessage {
	public enum RequestStatus {JOIN, LEAVE};
	public enum ResponseStatus {SUCCESS, FAILURE};
	public enum GameState {RESUME, PAUSE, EXIT};
	public enum SelectionState {PENDING, READY};
	
	public static class ConnectionRequest {
		public String playerID;
		public Date timeStamp;
		public RequestStatus type;
	}
	
	public static class ConnectionResponse {
		public ResponseStatus type;
	}
	
	public static class SelectionScreenInformation {
		public Map<Integer, String> playerList;
		public Map<String, SelectionState> playerStatusList;
	}
	
	public static class SelectionPlayerStatusInformation {
		public String playerID;
		public SelectionState state;
	}
	
	public static class GameStartInformation {}
	
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
}
