package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.not.itproject.screens.SelectionScreen.SelectionState;
import com.not.itproject.zero.ProjectZero;

public class GameScreen extends AbstractScreen {
	// declare variables
	GameState screenStatus;
	enum GameState { READY, RUNNING, PAUSED };
	
	// main constructor
	public GameScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		screenStatus = GameState.READY;
	}

	public void update(float delta) {
		// update objects
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(1f, 1f, 0.85f, 0.85f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// determine screen status
		switch (screenStatus) {
			case READY:
				// update objects
				update(delta);
				
				/** game render class function **/
				break;
				
			case RUNNING:
				// update objects
				update(delta);
				
				/** game render class function **/
				break;
				
			case PAUSED:
				// update objects
				update(delta);
				
				// paused function
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
