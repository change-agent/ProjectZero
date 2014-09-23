package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.objects.SimpleRoundButton;
import com.not.itproject.zero.ProjectZero;

public class SelectionScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnNext;
	SimpleButton btnReady;
	SimpleButton btnStart;
	SimpleRoundButton btnBack;
	SelectionState screenStatus;
	enum SelectionState { TRACK, VEHICLE, READY };
	private boolean setupNetwork = false;

	SimpleButton btnSendOne;
	
	// main constructor
	public SelectionScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		screenStatus = SelectionState.TRACK;
		int btnWidth = 80; // assign for main buttons
		int btnOffset = 10;
		btnNext = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnReady = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnStart = new SimpleButton((int)(gameWidth * 4/5) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnBack = new SimpleRoundButton(20, 20, 15);
		btnSendOne = new SimpleButton((int)(gameWidth / 2) - btnWidth/2, 
				(int)(gameHeight / 2), btnWidth, 30);
	}

	public void update(float delta) {
		// setup networking
		if (!setupNetwork && !NetworkHandler.isConnected()) {
			// initialise networking
			NetworkHandler.startGameSession();
			setupNetwork = true;
		}
		
		// update objects
		btnBack.update(delta);

		// test function/button
		btnSendOne.update(delta);
		if (btnSendOne.isTouched() && NetworkHandler.isClient()) {
			Gdx.app.log("NETWORK", "SEND MESSAGE");
			NetworkHandler.sendMessage("Connection Request.");
		}
		
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

		// check input from user and perform action
		if (btnNext.isTouched()) {
			// proceed to next state
			screenStatus = SelectionState.VEHICLE;

			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME, "Next button is pressed.");
		}
		else if (btnBack.isTouched()) {
			// go back to room
			game.nextScreen(ProjectZero.roomScreen, this);
			
			// disconnect from network
			if (NetworkHandler.isHost()) {
				// as host
				NetworkHandler.endGameSession();
			} else if (NetworkHandler.isClient()) {
				// as client
				NetworkHandler.leaveGameSession();
			}
		}
	}
	
	private void updateVehicle(float delta) {
		// update objects
		btnReady.update(delta);

		// check input from user and perform action
		if (btnReady.isTouched()) {
			// proceed to next state
			screenStatus = SelectionState.READY;
		}
		else if (btnBack.isTouched()) {
			// proceed to previous state
			screenStatus = SelectionState.TRACK;
		}
	}
	
	private void updateReady(float delta) {
		// session host - start option
		if (NetworkHandler.isHost()) {
			// update objects
			btnStart.update(delta);

			// check input from user and perform action
			if (btnStart.isTouched()) {
				// proceed to game
				game.nextScreen(ProjectZero.gameScreen, this);
			}
		}
		
		if (btnBack.isTouched()) {
			// proceed to previous state
			screenStatus = SelectionState.VEHICLE;
		}
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 1f, 0.85f, 0.85f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);
		
		// determine screen status
		switch (screenStatus) {
			case TRACK:
				// render track selection screen
				batch.begin();
				renderSessionTitle(delta);
				renderTrack(delta);		
				renderPlayers(delta);
				batch.end();
				break;
				
			case VEHICLE:
				// render vehicle selection screenHostHostHost
				batch.begin();
				renderSessionTitle(delta);
				renderVehicle(delta);		
				renderPlayers(delta);
				batch.end();				
				break;
				
			case READY:
				// render ready screen
				batch.begin();
				renderSessionTitle(delta);
				renderReady(delta);		
				renderPlayers(delta);
				batch.end();
				break;
		}
	}
	
	private void renderSessionTitle(float delta) {
		// render game session title
		AssetHandler.getFont(0.4f).draw(batch, "Game Session ID: " + 
				String.format("%05d", NetworkHandler.getGameSessionID()), 50, 10);
		
		AssetHandler.getFont(0.4f).draw(batch, "Host: " +	NetworkHandler.isHost() + " | " + 
				"Client: " +	NetworkHandler.isClient(), 60, 25);
	}
	
	private void renderPlayers(float delta) {
		// render number of players
		batch.draw(AssetHandler.playerOne, 10, 145, 50, 28);
		batch.draw(AssetHandler.playerOne, 63, 145, 50, 28);
		batch.draw(AssetHandler.playerOne, 116, 145, 50, 28);
		batch.draw(AssetHandler.playerOne, 169, 145, 50, 28);
	}
	
	private void renderTrack(float delta) {
		// render track menu
		batch.draw(AssetHandler.menuTrack, 10, 40, 300, 100);
		
		// render track state
		batch.draw(AssetHandler.buttonNext, 
				btnNext.getPosition().x, btnNext.getPosition().y, 
				btnNext.getWidth(), btnNext.getHeight());
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
		
		// test button
		if (NetworkHandler.isClient()) {
			batch.draw(AssetHandler.button, 
					btnSendOne.getPosition().x, btnSendOne.getPosition().y, 
					btnSendOne.getWidth(), btnSendOne.getHeight());					
		}	
	}
	
	private void renderVehicle(float delta) {
		// render vehicle menu
		batch.draw(AssetHandler.menuVehicle, 10, 40, 300, 100);
		
		// render vehicle state
		batch.draw(AssetHandler.buttonReady, 
				btnReady.getPosition().x, btnReady.getPosition().y, 
				btnReady.getWidth(), btnReady.getHeight());
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
		
	}

	private void renderReady(float delta) {
		// render ready menu
		batch.draw(AssetHandler.menuReady, 10, 40, 300, 100);
		
		// session host - start option
		if (NetworkHandler.isHost()) {
			// show start option
			batch.draw(AssetHandler.buttonStartSession, 
					btnStart.getPosition().x, btnStart.getPosition().y, 
					btnStart.getWidth(), btnStart.getHeight());
		}
		
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
		
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
