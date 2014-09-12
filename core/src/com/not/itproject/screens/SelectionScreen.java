package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.zero.ProjectZero;

public class SelectionScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnNext;
	SimpleButton btnReady;
	SimpleButton btnStart;
	SelectionState screenStatus;
	enum SelectionState { TRACK, VEHICLE, READY };
	
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
	}

	public void update(float delta) {
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
					Gdx.app.log(ProjectZero.GAME_NAME,
							"Next button is pressed.");
	
				}
				break;
	
			case VEHICLE:
				// update objects
				btnReady.update(delta);
	
				// check input from user and perform action
				if (btnReady.isTouched()) {
					// proceed to next state
					screenStatus = SelectionState.READY;
					
					// debug log to console
					Gdx.app.log(ProjectZero.GAME_NAME, "Ready button is pressed.");
	
				}
				break;
				
			case READY:
				// update objects
				btnStart.update(delta);
	
				// check input from user and perform action
				if (btnStart.isTouched()) {
					// proceed to game
					game.nextScreen(ProjectZero.gameScreen, this);
					
					// debug log to console
					Gdx.app.log(ProjectZero.GAME_NAME, "Start button is pressed.");
	
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
				batch.draw(AssetHandler.buttonNext, 
						btnNext.getPosition().x, btnNext.getPosition().y, 
						btnNext.getWidth(), btnNext.getHeight());
				batch.end();
				break;
				
			case VEHICLE:
				// render vehicle selection screen
				batch.begin();
				batch.draw(AssetHandler.buttonReady, 
						btnReady.getPosition().x, btnReady.getPosition().y, 
						btnReady.getWidth(), btnReady.getHeight());
				batch.end();				
				break;
				
			case READY:
				// render ready screen
				batch.begin();
				batch.draw(AssetHandler.buttonStartSession, 
						btnStart.getPosition().x, btnStart.getPosition().y, 
						btnStart.getWidth(), btnStart.getHeight());
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
