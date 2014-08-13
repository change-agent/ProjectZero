package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.not.itproject.objects.SimpleButton;
import com.not.itproject.objects.ToggleButton;
import com.not.itproject.zero.ProjectZero;

public class MainScreen extends AbstractScreen {
	// declare variables
	SimpleButton btnStart;
	SimpleButton btnHowToPlay;
	ToggleButton btnSoundToggle;
	
	// main constructor
	public MainScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		int btnWidth = 120; // assign for main buttons
		int btnOffset = 10;
		btnStart = new SimpleButton((int)(gameWidth * 1/4) - btnWidth/2 + btnOffset, 
				(int)(gameHeight/2), btnWidth, 30);
		btnHowToPlay = new SimpleButton((int)(gameWidth * 3/4) - btnWidth/2 - btnOffset, 
				(int)(gameHeight/2), btnWidth, 30);
		
		btnWidth = 80; // reassign for sound toggle
		btnSoundToggle = new ToggleButton((int)(gameWidth / 2) - btnWidth/2, 
				(int)(gameHeight * 4/5), btnWidth, 20);
	}

	public void update(float delta) {
		// update objects
		btnStart.update(delta);
		btnHowToPlay.update(delta);
		btnSoundToggle.update(delta);
		
		// check input from user and perform action
		if (btnStart.isTouched()) {
			game.setScreen(ProjectZero.roomScreen);
			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME, "Start button is pressed.");
			
		} else if (btnHowToPlay.isTouched()) {
			game.setScreen(ProjectZero.helpScreen);
			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME, "How-to-play button is pressed.");
			
		} else if (btnSoundToggle.isTouched()) {
			// debug log to console
			Gdx.app.log(ProjectZero.GAME_NAME, "Sound button is toggled. " + 
					"Toggle on: " + btnSoundToggle.isToggleOn());
		}
	}
	
	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(0.65f, 0.65f, 0.85f, 0.85f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// update objects
		update(delta);
		
		// render screen
		batch.begin();
		batch.end();
		
		// render shapes
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(btnStart.getPosition().x, btnStart.getPosition().y, 
				btnStart.getWidth(), btnStart.getHeight(), 0, 0, 0);
		shapeRenderer.rect(btnHowToPlay.getPosition().x, btnHowToPlay.getPosition().y, 
				btnHowToPlay.getWidth(), btnHowToPlay.getHeight(), 0, 0, 0);
		shapeRenderer.rect(btnSoundToggle.getPosition().x, btnSoundToggle.getPosition().y, 
				btnSoundToggle.getWidth(), btnSoundToggle.getHeight(), 0, 0, 0);
		shapeRenderer.end();
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