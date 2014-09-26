package com.not.itproject.networks;

import java.util.Date;
import java.util.Map;

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
}
