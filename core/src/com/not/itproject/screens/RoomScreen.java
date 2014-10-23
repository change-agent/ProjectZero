package com.not.itproject.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.objects.SimpleRoundButton;
import com.not.itproject.zero.ProjectZero;

public class RoomScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnLoadGame;
	SimpleButton btnNewGame;
//	SimpleButton btnLoad;
	SimpleRoundButton btnBack;
	SimpleRoundButton btnRefresh;
	RoomState screenStatus;
	enum RoomState { WAITING, LOAD };
	
	// declare buttons for showing sessions
	private boolean setupNetwork = false;
	Map<Integer, SimpleButton> btnGameSessions;
	Map<Integer, SimpleButton> btnSavedGameSessions;
	
	// main constructor
	public RoomScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		screenStatus = RoomState.WAITING;
		int btnWidth = 120; // assign for main buttons
		int btnOffset = 10;
		btnLoadGame = new SimpleButton((int)(gameWidth * 1/4) - btnWidth/2 + btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnNewGame = new SimpleButton((int)(gameWidth * 3/4) - btnWidth/2 - btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
//		btnLoad = new SimpleButton((int)(gameWidth * 3/4) - btnWidth/2 - btnOffset, 
//				(int)(gameHeight * 4/5), btnWidth, 30);
		btnBack = new SimpleRoundButton(20, 20, 15);
		btnRefresh = new SimpleRoundButton((int)gameWidth - 20, 20, 15);
		
		// variable for storing session buttons
		btnGameSessions = new HashMap<Integer, SimpleButton>();
		btnSavedGameSessions = new HashMap<Integer, SimpleButton>();
	}

	private void detectGameSessions() {
		// discover servers
		NetworkHandler.getNetworkClient().discoverServers();
		
		// clear button objects
		btnGameSessions.clear();
		
		// add button object
		int btnOffset = 40;
		int btnWidth = (int) (gameWidth - btnOffset*2);
		
		// iterate and add buttons to join session
		for (int i=0; i < NetworkHandler.getNetworkClient().getListOfServers().size(); i++) {
			if (NetworkHandler.getNetworkClient().getListOfServers().get(i) != null) {
				btnGameSessions.put(i, new SimpleButton(btnOffset - 20, 
						btnOffset + (33 * i), btnWidth, 30));
			}
		}
	}
	
	private void getSavedGameSessions() {		
		// clear button objects
		btnSavedGameSessions.clear();
		
		// add button object
		int btnOffset = 40;
		int btnWidth = (int) (gameWidth - btnOffset*2);
		
		// iterate and add buttons to load session
		for (int i=0; i < AssetHandler.getSavedGameSessions().size(); i++) {
			// host if player was previously host
			if (AssetHandler.getSavedGameSessions().get(i) != null &&
					AssetHandler.getSavedGameSessions().get(i)
					.getPlayerByPlayerID(AssetHandler.getPlayerID()) != null &&
					AssetHandler.getSavedGameSessions().get(i)
					.getPlayerByPlayerID(AssetHandler.getPlayerID()).index == 0) {
				// add button
				btnSavedGameSessions.put(i, new SimpleButton(btnOffset - 20, 
						btnOffset + (33 * i), btnWidth, 30));
			}
		}
	}

	public void update(float delta) {
		// setup networking
		if (!setupNetwork && screenStatus == RoomState.WAITING) {
			// initialise networking
			NetworkHandler.client.startClient();
//			detectGameSessions();
			setupNetwork = true;
		}
		
		// determine screen status
		switch (screenStatus) {
			case WAITING:
				updateWaiting(delta);
				break;
	
			case LOAD:
				updateLoad(delta);
				break;
		}
	}
	
	private void updateWaiting(float delta) {
		// update objects
		btnLoadGame.update(delta);
		btnNewGame.update(delta);
		btnBack.update(delta);
		btnRefresh.update(delta);
		
		// check input from user and perform action
		for (int i = 0; i < NetworkHandler.getNetworkClient().getListOfServers().size(); i++) {
			if (NetworkHandler.getNetworkClient().getListOfServers().get(i) != null) {
				// render session buttons
				btnGameSessions.get(i).update(delta);
				
				// handle user input
				if (btnGameSessions.get(i).isTouched()) {
					// connect to game session
					NetworkHandler.getNetworkClient().connectToGameSession(i);
				}
			}
		}
		
		if (btnLoadGame.isTouched()) {
			// proceed to loading a game
			setupNetwork = false;
			screenStatus = RoomState.LOAD;
			
			// generate buttons
			AssetHandler.loadAllGameSessions();
			getSavedGameSessions();
			
			// debug log to console
			ProjectZero.log("Load Game button is pressed.");

		} else if (btnNewGame.isTouched()) {
			// debug log to console
			game.nextScreen(ProjectZero.selectionScreen, this);
			ProjectZero.log("New Game button is pressed.");
			
		} else if (btnBack.isTouched()) {
			// proceed back to main menu
			setupNetwork = false;
			game.nextScreen(ProjectZero.mainScreen, this);
			
		} else if (btnRefresh.isTouched()) {
			// refresh network - scan for sessions
			detectGameSessions();
		}
	}
	
	private void updateLoad(float delta) {
		// update objects
//		btnLoad.update(delta);
		btnBack.update(delta);
		
		// check input from user and perform action
		for (int i = 0; i < btnSavedGameSessions.size(); i++) {
			if (AssetHandler.getSavedGameSessions().get(i) != null) {
				// render session buttons
				btnSavedGameSessions.get(i).update(delta);
				
				// handle user input
				if (btnSavedGameSessions.get(i).isTouched()) {
					// load to game session
					ProjectZero.gameSession.loadGameSession(AssetHandler.getSavedGameSessions().get(i));
					
					// return user back to waiting state
					screenStatus = RoomState.WAITING;
					
					// proceed to next room
					game.nextScreen(ProjectZero.selectionScreen, this);
				}
			}
		}
		
		// check input from user and perform action
//		if (btnLoad.isTouched()) {
//			// debug log to console
//			ProjectZero.log("Load button is pressed.");
//
//		} 
		if (btnBack.isTouched()) {
			// proceed back to waiting
			screenStatus = RoomState.WAITING;
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
			case WAITING:
				// render room (waiting) screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				renderWaiting(delta);
				batch.end();
				break;
				
			case LOAD:
				// render load screen
				batch.begin();
				
				// draw background
				batch.draw(AssetHandler.backgroundUniversal, 0, 0,
						gameWidth, gameHeight);
				renderLoad(delta);
				batch.end();
				break;
		}
	}
	
	private void renderWaiting(float delta) {
		// render waiting state

		// display all game sessions
		for (int i=0; i < NetworkHandler.getNetworkClient().getListOfServers().size(); i++) {
			if (NetworkHandler.getNetworkClient().getListOfServers().get(i) != null) {
				// render session buttons
				btnGameSessions.get(i).update(delta);
				batch.draw(AssetHandler.button,
						btnGameSessions.get(i).getPosition().x, btnGameSessions.get(i).getPosition().y,
						btnGameSessions.get(i).getWidth(), btnGameSessions.get(i).getHeight());
				
				// button text  
				AssetHandler.getFont(0.4f).draw(batch, "Game Session ID: " + String.format("%05d", i), 
						btnGameSessions.get(i).getPosition().x + 10, 
						btnGameSessions.get(i).getPosition().y + 8);
			}
		}

		// display screen state buttons
		batch.draw(AssetHandler.buttonLoadGame,
				btnLoadGame.getPosition().x, btnLoadGame.getPosition().y,
			btnLoadGame.getWidth(), btnLoadGame.getHeight());
		batch.draw(AssetHandler.buttonNewGame, 
				btnNewGame.getPosition().x, btnNewGame.getPosition().y, 
				btnNewGame.getWidth(), btnNewGame.getHeight());
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
		batch.draw(AssetHandler.buttonRefresh, 
				btnRefresh.getPosition().x - btnRefresh.getRadius(), 
				btnRefresh.getPosition().y - btnRefresh.getRadius(), 
				btnRefresh.getRadius() * 2, btnRefresh.getRadius() * 2);
	}

	private void renderLoad(float delta) {
		// display all saved game sessions
		for (int i=0; i < btnSavedGameSessions.size(); i++) {
			if (AssetHandler.getSavedGameSessions().get(i) != null) {
				// render saved session buttons
				btnSavedGameSessions.get(i).update(delta);
				batch.draw(AssetHandler.button,
						btnSavedGameSessions.get(i).getPosition().x, btnSavedGameSessions.get(i).getPosition().y,
						btnSavedGameSessions.get(i).getWidth(), btnSavedGameSessions.get(i).getHeight());
				
				// button text  
				AssetHandler.getFont(0.3f).draw(batch, "Saved Date:", 
						btnSavedGameSessions.get(i).getPosition().x + 8, 
						btnSavedGameSessions.get(i).getPosition().y + 6);
				AssetHandler.getFont(0.25f).draw(batch, AssetHandler.getSavedGameSessions().get(i).getLastSaved(), 
						btnSavedGameSessions.get(i).getPosition().x + 15, 
						btnSavedGameSessions.get(i).getPosition().y + 18);
			}
		}
		
		// render load state
//		batch.draw(AssetHandler.buttonLoad, 
//				btnLoad.getPosition().x, btnLoad.getPosition().y, 
//				btnLoad.getWidth(), btnLoad.getHeight());
		batch.draw(AssetHandler.buttonBack, 
				btnBack.getPosition().x - btnBack.getRadius(), 
				btnBack.getPosition().y - btnBack.getRadius(), 
				btnBack.getRadius() * 2, btnBack.getRadius() * 2);
	}	

	public void enterGameSession() {
		// determine whether connection success
		if (NetworkHandler.isClient()) {
			// enter game session
			game.nextScreen(ProjectZero.selectionScreen, this);			
		}
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
