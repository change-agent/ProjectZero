package com.not.itproject.zero;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.screens.SplashScreen;

public class ProjectZero extends Game {
	// declare variables
	public static final String GAME_NAME = "Project Zero";
	public static final int GAME_WIDTH = 320;
	public static int GAME_HEIGHT;
	public static float RATIO;
	
	@Override
	public void create () {
		// declare variables
		RATIO = (float) GAME_WIDTH / Gdx.graphics.getWidth();
		
		// load all assets
		AssetHandler.load();
		
		// proceed to splash screen
		setScreen(new SplashScreen(this));
	}
	
	public void dispose() {
		// release all assets
		AssetHandler.release();
	}
}
