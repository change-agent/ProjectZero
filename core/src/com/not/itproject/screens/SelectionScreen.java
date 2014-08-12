package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.not.itproject.zero.ProjectZero;

public class SelectionScreen extends AbstractScreen {
	// declare variables
	SelectionState screenStatus;
	enum SelectionState { TRACK, VEHICLE, READY };
	
	// main constructor
	public SelectionScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		screenStatus = SelectionState.TRACK;
	}

	public void update(float delta) {
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
				batch.end();
				
				// render shapes
				shapeRenderer.begin(ShapeType.Line);
				shapeRenderer.end();
				break;
				
			case VEHICLE:
				// render vehicle selection screen
				batch.begin();
				batch.end();
				
				// render shapes
				shapeRenderer.begin(ShapeType.Line);
				shapeRenderer.end();				
				break;
				
			case READY:
				// render ready screen
				batch.begin();
				batch.end();
				
				// render shapes
				shapeRenderer.begin(ShapeType.Line);
				shapeRenderer.end();
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
