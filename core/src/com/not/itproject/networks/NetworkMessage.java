package com.not.itproject.networks;

import java.util.Date;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class NetworkMessage {
	enum RequestStatus {JOIN, LEAVE};
	
	public static class ConnectionRequest {
		public String playerID;
		public Date timeStamp;
		public RequestStatus type;
	}
	
	public static class ConnectionResponse {
		public String message;
	}
	
	public static class SelectionScreenInformation {
		public Map<Integer, String> playerList;
	}
	
	public static class GameStartInformation {}
	
	public static class GameCarInformation {
		public String playerID;
		public Vector2 position;
		public Vector2 velocity;
		public float rotation;
	}
}
