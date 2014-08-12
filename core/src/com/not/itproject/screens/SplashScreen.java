package com.not.itproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.zero.ProjectZero;

public class SplashScreen extends AbstractScreen {
	// declare variables	
	private float runtime;
	private static final float DELAY_TIME = 2;
	
	// main constructor
	public SplashScreen(ProjectZero game) {
		// define super
		super(game);
		
		// initialise variables
		runtime = 0;
	}

	@Override
	public void render(float delta) {
		// clear screen
		Gdx.graphics.getGL20().glClearColor(0.75f, 0.75f, 0.75f, 0.5f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

		// render screen
		batch.begin();
		batch.draw(AssetHandler.logo, 
				gameWidth/2 - AssetHandler.logo.getRegionWidth()/2, 
				gameHeight/2 - AssetHandler.logo.getRegionHeight()/2, 
				AssetHandler.logo.getRegionWidth(), 
				AssetHandler.logo.getRegionHeight());
		batch.end();
		
		// calculate runtime
		runtime += delta;
		if (runtime > DELAY_TIME) {
			// proceed to menu after 2 seconds
			game.setScreen(new MainScreen(game));
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
