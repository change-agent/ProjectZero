package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.zero.ProjectZero;

public class RoomScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnLoadGame;
	SimpleButton btnNewGame;
	SimpleButton btnLoad;
	RoomState screenStatus;
	enum RoomState { WAITING, LOAD };
	
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
	}

	public void update(float delta) {
		// determine screen status
		switch (screenStatus) {
			case WAITING:
				// update objects
				btnLoadGame.update(delta);
				btnNewGame.update(delta);
				
				// check input from user and perform action
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
				}
				break;
	
			case LOAD:
				// update objects
				btnLoad.update(delta);
				
				// check input from user and perform action
				if (btnLoad.isTouched()) {
					// debug log to console
					Gdx.app.log(ProjectZero.GAME_NAME,
							"Load button is pressed.");
	
				}
				break;
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
				batch.draw(AssetHandler.buttonLoadGame, 
						btnLoadGame.getPosition().x, btnLoadGame.getPosition().y, 
						btnLoadGame.getWidth(), btnLoadGame.getHeight());
				batch.draw(AssetHandler.buttonNewGame, 
						btnNewGame.getPosition().x, btnNewGame.getPosition().y, 
						btnNewGame.getWidth(), btnNewGame.getHeight());
				batch.end();
				break;
				
			case LOAD:
				// render load screen
				batch.begin();
				batch.draw(AssetHandler.buttonLoad, 
						btnLoad.getPosition().x, btnLoad.getPosition().y, 
						btnLoad.getWidth(), btnLoad.getHeight());
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
