package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	BitmapFont font;
	
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
		
		font = new BitmapFont();
		font.setScale(1, -1);
		font.setColor(Color.BLACK);
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
		
		// determine screen status
		switch (screenStatus) {
			case TRACK:
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
						NetworkHandler.endGameSession();
					} else if (NetworkHandler.isClient()) {
						NetworkHandler.leaveGameSession();
					}
				}
				break;
	
			case VEHICLE:
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
				break;
				
			case READY:
				// update objects
				btnStart.update(delta);
	
				// check input from user and perform action
				if (btnStart.isTouched()) {
					// proceed to game
					game.nextScreen(ProjectZero.gameScreen, this);
				}
				else if (btnBack.isTouched()) {
					// proceed to previous state
					screenStatus = SelectionState.VEHICLE;
				}
				break;
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
				font.draw(batch, "Host: " +	NetworkHandler.isHost() + " | " + 
						"Client: " +	NetworkHandler.isClient(), 50, 10);
				batch.draw(AssetHandler.buttonNext, 
						btnNext.getPosition().x, btnNext.getPosition().y, 
						btnNext.getWidth(), btnNext.getHeight());
				batch.draw(AssetHandler.buttonBack, 
						btnBack.getPosition().x - btnBack.getRadius(), 
						btnBack.getPosition().y - btnBack.getRadius(), 
						btnBack.getRadius() * 2, btnBack.getRadius() * 2);
				batch.end();
				break;
				
			case VEHICLE:
				// render vehicle selection screenHostHostHost
				batch.begin();
				batch.draw(AssetHandler.buttonReady, 
						btnReady.getPosition().x, btnReady.getPosition().y, 
						btnReady.getWidth(), btnReady.getHeight());
				batch.draw(AssetHandler.buttonBack, 
						btnBack.getPosition().x - btnBack.getRadius(), 
						btnBack.getPosition().y - btnBack.getRadius(), 
						btnBack.getRadius() * 2, btnBack.getRadius() * 2);
				batch.end();				
				break;
				
			case READY:
				// render ready screen
				batch.begin();
				
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
				batch.end();
				break;
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
