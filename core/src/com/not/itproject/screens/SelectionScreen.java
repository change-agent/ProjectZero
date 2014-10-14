package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.networks.NetworkMessage;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.objects.SimpleRoundButton;
import com.not.itproject.zero.ProjectZero;

public class SelectionScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnNext;
	SimpleButton btnReady;
	SimpleButton btnStart;
	SimpleRoundButton btnBack;
	SimpleRoundButton btnNavigateLeft;
	SimpleRoundButton btnNavigateRight;
	SelectionState screenStatus;
	TrackType selectedTrack;
	VehicleType selectedVehicle;
	public enum SelectionState 	{ TRACK, VEHICLE, READY };
	public enum TrackType 		{ NONE, TRACK_ONE }; 
		//{ TRACK_ONE, TRACK_TWO, TRACK_THREE };		// core to implement multiple tracks
	public enum VehicleType		{ NONE, VEHICLE_ONE }; 
		//{ VEHICLE_ONE, VEHICLE_TWO, VEHICLE_THREE };	// core to implement multiple vehicles
	private boolean setupNetwork = false;
	
	// main constructor
	public SelectionScreen(ProjectZero game) {
		// define super 
		super(game);
		
		// initialise variables
		screenStatus = SelectionState.TRACK;
		selectedTrack = TrackType.NONE;
		selectedVehicle = VehicleType.NONE;
		int btnWidth = 80; // assign for main buttons
		int btnOffset = 10;
		btnNext = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnReady = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnStart = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnBack = new SimpleRoundButton(20, 20, 15);
		btnNavigateLeft = new SimpleRoundButton(32, 95, 12);
		btnNavigateRight = new SimpleRoundButton(200, 95, 12);
	}

	public void update(float delta) {
		/* setup networking */
		if (!setupNetwork && !NetworkHandler.isClient()) {
			// create a new game session as host
			NetworkHandler.getNetworkServer().startGameSession();
			setupNetwork = true;
		} else if (!setupNetwork && NetworkHandler.isClient()) {
			// determine network has been setup as client
			setupNetwork = true;
		}
		
		// handle client disconnection from server
		if (setupNetwork && !NetworkHandler.isClient() &&
				!NetworkHandler.isHost()) {
			// return back to room screen displaying message
			ProjectZero.log("Network - Disconnected");
			setupNetwork = false;
		}
		
		// host - send selection screen information to clients
		if (setupNetwork && NetworkHandler.isHost() &&
				NetworkHandler.getNetworkServer().getServer().getConnections().length > 0) {
			// send information to client
			NetworkHandler.getNetworkServer().sendSelectionScreenInformation();
		}
		
		// update objects
		btnBack.update(delta);
		
		// determine screen status
		switch (screenStatus) {
			case TRACK:
				updateTrack(delta);
				break;
	
			case VEHICLE:
				updateVehicle(delta);
				break;
				
			case READY:
				updateReady(delta);
				break;
		}
	}
	
	private void updateTrack(float delta) {
		// update objects
		btnNext.update(delta);
		btnNavigateLeft.update(delta);
		btnNavigateRight.update(delta);

		// check input from user and perform action
		if (btnNext.isTouched()) {
			// proceed to next state
			screenStatus = SelectionState.VEHICLE;

			// debug log to console
			ProjectZero.log("Next button is pressed.");
		}
		else if (btnBack.isTouched()) {
			// disconnect from network
			if (NetworkHandler.isHost()) {
				// as host - end game session
				NetworkHandler.getNetworkServer().endGameSession();
			} else if (NetworkHandler.isClient()) {
				// as client - leave game session
				NetworkHandler.getNetworkClient().leaveGameSession();
			}
			
			// go back to room
			setupNetwork = false;
			game.nextScreen(ProjectZero.roomScreen, this);
		}
	}
	
	private void updateVehicle(float delta) {
		// update objects
		btnReady.update(delta);
		btnNavigateLeft.update(delta);
		btnNavigateRight.update(delta);

		// check input from user and perform action
		if (btnReady.isTouched()) {
			// proceed to next state
			screenStatus = SelectionState.READY;
			
			// client - send ready state
			if (NetworkHandler.isClient() && 
					screenStatus == SelectionState.READY) {
				// send information to server
				NetworkHandler.getNetworkClient().sendSelectionScreenReadyInformation(NetworkMessage.SelectionState.READY);
			}
			else if (NetworkHandler.isHost()) {
				// set host as ready
				NetworkHandler.getListOfPlayerStatus().put(AssetHandler.getPlayerID(), NetworkMessage.SelectionState.READY);
			}
		}
		else if (btnBack.isTouched()) {
			// proceed to previous state
			screenStatus = SelectionState.TRACK;
		}
	}
	
	private void updateReady(float delta) {
		// session host - start option
		if (NetworkHandler.isHost() && allPlayersReady()) {
			// update objects
			btnStart.update(delta);

			// check input from user and perform action
			if (btnStart.isTouched()) {
				// proceed to game
				NetworkHandler.getNetworkServer().sendGameStart();
				startGame();
			}
		}
		
		if (btnBack.isTouched()) {
			// proceed to previous state
			screenStatus = SelectionState.VEHICLE;

			// client - send pending state
			if (NetworkHandler.isClient() && 
					screenStatus != SelectionState.READY) {
				// send information to server
				NetworkHandler.getNetworkClient().sendSelectionScreenReadyInformation(NetworkMessage.SelectionState.PENDING);
			}
			else if (NetworkHandler.isHost()) {
				// set host as pending
				NetworkHandler.getListOfPlayerStatus().put(AssetHandler.getPlayerID(), NetworkMessage.SelectionState.PENDING);
			}
		}
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 1f, 1f, 1f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);
		
		// determine screen status
		switch (screenStatus) {
			case TRACK:
				// render track selection screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				renderTrack(delta);		
				renderPlayers(delta);
				renderSessionTitle(delta);
				batch.end();
				break;
				
			case VEHICLE:
				// render vehicle selection screenHostHostHost
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				renderVehicle(delta);		
				renderPlayers(delta);
				renderSessionTitle(delta);
				batch.end();				
				break;
				
			case READY:
				// render ready screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				
				renderReady(delta);		
				renderPlayers(delta);
				renderSessionTitle(delta);
				batch.end();
				break;
		}
	}
	
	private void renderSessionTitle(float delta) {
		// render banner
		batch.draw(AssetHandler.bannerWhite, 0, 3, 0, 0, 
				gameWidth, 35, 1, 1, 0);
		
		// render game session title
		AssetHandler.getFont(0.5f).draw(batch, "Game Session ID: " + 
				String.format("%05d", NetworkHandler.getGameSessionID()), 
				50, 15);
		
		// determine message to display
		String message = null;
		switch (screenStatus) {
			case TRACK:
				message = "Select a track:";
				break;
			case VEHICLE:
				message = "Select a vehicle:";
				break;
			case READY:
				message = "Awaiting other players";
				break;
		}
		
		// if all players are ready
		if (allPlayersReady()) {
			message = "All players ready!";
		}
		
		// render message
		AssetHandler.getFont(0.35f).draw(batch, message, 22, 50);
		
		// draw back button
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
	}
	
	private void renderPlayers(float delta) {
		// render number of players
		if (NetworkHandler.getListOfPlayers().containsKey(0)) {
			// number of players = 1 (host)
			batch.draw(AssetHandler.playerRed, 10, 145, 50, 28);
		} else {

			batch.draw(AssetHandler.playerNA, 10, 145, 50, 28);
		}
		
		if (NetworkHandler.getListOfPlayers().containsKey(1)) {
			// number of players = 2
			batch.draw(AssetHandler.playerBlue, 63, 145, 50, 28);
		} else {

			batch.draw(AssetHandler.playerNA, 63, 145, 50, 28);
		}
		
		if (NetworkHandler.getListOfPlayers().containsKey(2)) {
			// number of players = 3
			batch.draw(AssetHandler.playerGreen, 116, 145, 50, 28);
		} else {

			batch.draw(AssetHandler.playerNA, 116, 145, 50, 28);
		}
		
		if (NetworkHandler.getListOfPlayers().containsKey(3)) {
			// number of players = 4
			batch.draw(AssetHandler.playerYellow, 169, 145, 50, 28);
		} else {

			batch.draw(AssetHandler.playerNA, 169, 145, 50, 28);
		}
		
		// mark which player is you
		if (NetworkHandler.getListOfPlayers().containsKey(0) &&
				NetworkHandler.getListOfPlayers().get(0).contains(AssetHandler.getPlayerID())) {
			AssetHandler.getFont(0.5f).draw(batch, "Me", 22, 150);
			
		} else if (NetworkHandler.getListOfPlayers().containsKey(1) &&
				NetworkHandler.getListOfPlayers().get(1).contains(AssetHandler.getPlayerID())) {
			AssetHandler.getFont(0.5f).draw(batch, "Me", 74, 150);
			
		} else if (NetworkHandler.getListOfPlayers().containsKey(2) &&
				NetworkHandler.getListOfPlayers().get(2).contains(AssetHandler.getPlayerID())) {
			AssetHandler.getFont(0.5f).draw(batch, "Me", 127, 150);
			
		} else if (NetworkHandler.getListOfPlayers().containsKey(3) &&
				NetworkHandler.getListOfPlayers().get(3).contains(AssetHandler.getPlayerID())) {
			AssetHandler.getFont(0.5f).draw(batch, "Me", 180, 150);
		}
			
		// mark which player is host
		AssetHandler.getFont(0.25f).draw(batch, "Host", 23, 163);
		
		// mark which player is ready
		if (NetworkHandler.getListOfPlayers().containsKey(0) &&
				NetworkHandler.getListOfPlayerStatus().get(
						NetworkHandler.getListOfPlayers().get(0)) 
						== (NetworkMessage.SelectionState.READY)) {
			// show ready badge
			batch.draw(AssetHandler.playerReady, 10, 145, 50, 28);
		}
		if (NetworkHandler.getListOfPlayers().containsKey(1) &&
				NetworkHandler.getListOfPlayerStatus().get(
						NetworkHandler.getListOfPlayers().get(1)) 
						== (NetworkMessage.SelectionState.READY)) {
			// show ready badge
			batch.draw(AssetHandler.playerReady, 63, 145, 50, 28);
		}
		if (NetworkHandler.getListOfPlayers().containsKey(2) &&
				NetworkHandler.getListOfPlayerStatus().get(
						NetworkHandler.getListOfPlayers().get(2)) 
						== (NetworkMessage.SelectionState.READY)) {
			// show ready badge
			batch.draw(AssetHandler.playerReady, 116, 145, 50, 28);
		}
		if (NetworkHandler.getListOfPlayers().containsKey(3) &&
				NetworkHandler.getListOfPlayerStatus().get(
						NetworkHandler.getListOfPlayers().get(3)) 
						== (NetworkMessage.SelectionState.READY)) {
			// show ready badge
			batch.draw(AssetHandler.playerReady, 169, 145, 50, 28);
		}
	}
	
	private void renderTrack(float delta) {
		// render track menu
		batch.draw(AssetHandler.menuTrack, 10, 40, 300, 100);
		batch.draw(AssetHandler.navigateLeft, 
				btnNavigateLeft.getPosition().x - btnNavigateLeft.getRadius(), 
				btnNavigateLeft.getPosition().y - btnNavigateLeft.getRadius(), 
				btnNavigateLeft.getRadius() * 2, btnNavigateLeft.getRadius() * 2);
		batch.draw(AssetHandler.navigateRight, 
				btnNavigateRight.getPosition().x - btnNavigateRight.getRadius(), 
				btnNavigateRight.getPosition().y - btnNavigateRight.getRadius(), 
				btnNavigateRight.getRadius() * 2, btnNavigateRight.getRadius() * 2);
		
		// render track state
		batch.draw(AssetHandler.buttonNext, 
				btnNext.getPosition().x, btnNext.getPosition().y, 
				btnNext.getWidth(), btnNext.getHeight());
	}

	private void renderVehicle(float delta) {
		// render vehicle menu
		batch.draw(AssetHandler.menuVehicle, 10, 40, 300, 100);
		batch.draw(AssetHandler.navigateLeft, 
				btnNavigateLeft.getPosition().x - btnNavigateLeft.getRadius(), 
				btnNavigateLeft.getPosition().y - btnNavigateLeft.getRadius(), 
				btnNavigateLeft.getRadius() * 2, btnNavigateLeft.getRadius() * 2);
		batch.draw(AssetHandler.navigateRight, 
				btnNavigateRight.getPosition().x - btnNavigateRight.getRadius(), 
				btnNavigateRight.getPosition().y - btnNavigateRight.getRadius(), 
				btnNavigateRight.getRadius() * 2, btnNavigateRight.getRadius() * 2);
		
		// render vehicle state
		batch.draw(AssetHandler.buttonReady, 
				btnReady.getPosition().x, btnReady.getPosition().y, 
				btnReady.getWidth(), btnReady.getHeight());
		
		// render vehicle
		batch.draw(AssetHandler.carRed, 148, 50, 
				AssetHandler.carRed.getRegionWidth()*0.05f/2, AssetHandler.carRed.getRegionHeight()*0.05f/2, 
				AssetHandler.carRed.getRegionWidth(), AssetHandler.carRed.getRegionHeight(), 
				0.10f, 0.10f, 90);		
	}

	private void renderReady(float delta) {
		// render ready menu
		batch.draw(AssetHandler.menuReady, 10, 40, 300, 100);
		
		// session host - start option
		if (NetworkHandler.isHost() && allPlayersReady()) {
			// show start option
			batch.draw(AssetHandler.buttonStartSession, 
					btnStart.getPosition().x, btnStart.getPosition().y, 
					btnStart.getWidth(), btnStart.getHeight());
		}
		
	}
	
	public void startGame() {
		// mark game start
		NetworkHandler.setGameStart(true);
		
		// proceed to game screen
		game.nextScreen(ProjectZero.gameScreen, this);
	}
	
	private boolean allPlayersReady() {
		boolean result = true;
		for (NetworkMessage.SelectionState status : NetworkHandler.getListOfPlayerStatus().values()) {
			// determine if all players are ready
			if (status != NetworkMessage.SelectionState.READY) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		game.dispose();
	}
}
