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
	SimpleButton btnLoad;
	SimpleRoundButton btnBack;
	SimpleRoundButton btnRefresh;
	RoomState screenStatus;
	enum RoomState { WAITING, LOAD };
	
	private float elapsedNetworkRefresh = 0;
	private float refreshDelay = 10;
	private boolean setupNetwork = false;
	
	Map<Integer, SimpleButton> btnGameSessions;
	
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
		btnLoad = new SimpleButton((int)(gameWidth * 3/4) - btnWidth/2 - btnOffset, 
				(int)(gameHeight * 4/5), btnWidth, 30);
		btnBack = new SimpleRoundButton(20, 20, 15);
		btnRefresh = new SimpleRoundButton((int)gameWidth - 20, 20, 15);
		
		// variable for storing session buttons
		btnGameSessions = new HashMap<Integer, SimpleButton>();
	}

	private void detectGameSessions() {
		// discover servers
		NetworkHandler.discoverServer();
		
		// clear button objects
		btnGameSessions.clear();
		
		// add button object
		int btnOffset = 40;
		int btnWidth = (int) (gameWidth - btnOffset*2);
		
		// iterate and add buttons to join session
		for (int i=0; i < NetworkHandler.getListOfServers().size(); i++) {
			if (NetworkHandler.getListOfServers().get(i) != null) {
				btnGameSessions.put(i, new SimpleButton(btnOffset - 20, 
						btnOffset + (33 * i), btnWidth, 30));
			}
		}
	}

	private void autoDetectSessions(float delta) {
		// auto detecting game sessions
		elapsedNetworkRefresh += delta;
		if (!NetworkHandler.isConnected() && elapsedNetworkRefresh > refreshDelay) {
			detectGameSessions();
			elapsedNetworkRefresh = 0;
		}	
	}
	
	public void update(float delta) {
		// setup networking
		if (!setupNetwork) {
			// initialise networking
			NetworkHandler.setupClient();
			detectGameSessions();
			setupNetwork = true;
		}
		
		// auto detecting game sessions
//		autoDetectSessions(delta);
		
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
		for (int i=0; i < NetworkHandler.getListOfServers().size(); i++) {
			if (NetworkHandler.getListOfServers().get(i) != null) {
				// render session buttons
				btnGameSessions.get(i).update(delta);
				
				// handle user input
				if (btnGameSessions.get(i).isTouched()) {
					// connect to game session
					NetworkHandler.connectToGameSession(i);
					
					// determine whether connection success
					if (NetworkHandler.isConnected()) {
						game.nextScreen(ProjectZero.selectionScreen, this);
					}
				}
			}
		}
		
		if (btnLoadGame.isTouched()) {
			// proceed to loading a game
			screenStatus = RoomState.LOAD;
			
			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME,
					"Load Game button is pressed.");

		} else if (btnNewGame.isTouched()) {
			// debug log to console
			game.nextScreen(ProjectZero.selectionScreen, this);
			Gdx.app.log(ProjectZero.GAME_NAME,
					"New Game button is pressed.");
			
		} else if (btnBack.isTouched()) {
			// proceed back to main menu
			game.nextScreen(ProjectZero.mainScreen, this);
			
		} else if (btnRefresh.isTouched()) {
			// refresh network - scan for sessions
			detectGameSessions();
		}
	}
	
	private void updateLoad(float delta) {
		// update objects
		btnLoad.update(delta);
		btnBack.update(delta);
		
		// check input from user and perform action
		if (btnLoad.isTouched()) {
			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME,
					"Load button is pressed.");

		} else if (btnBack.isTouched()) {
			// proceed back to waiting
			screenStatus = RoomState.WAITING;
		}
	}
	

	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(0.65f, 1f, 0.85f, 0.85f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);
		
		// determine screen status
		switch (screenStatus) {
			case WAITING:
				// render room (waiting) screen
				batch.begin();
				renderWaiting(delta);
				batch.end();
				break;
				
			case LOAD:
				// render load screen
				batch.begin();
				renderLoad(delta);
				batch.end();
				break;
		}
	}
	
	private void renderWaiting(float delta) {
		// render waiting state

		// display all game sessions
		for (int i=0; i < NetworkHandler.getListOfServers().size(); i++) {
			if (NetworkHandler.getListOfServers().get(i) != null) {
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
		// render load state
		batch.draw(AssetHandler.buttonLoad, 
				btnLoad.getPosition().x, btnLoad.getPosition().y, 
				btnLoad.getWidth(), btnLoad.getHeight());
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
