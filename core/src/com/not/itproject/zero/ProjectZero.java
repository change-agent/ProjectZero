package com.not.itproject.zero;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.screens.HelpScreen;
import com.not.itproject.screens.MainScreen;
import com.not.itproject.screens.RoomScreen;
import com.not.itproject.screens.SelectionScreen;
import com.not.itproject.screens.SplashScreen;

public class ProjectZero extends Game {
	// declare variables
	public static final String GAME_NAME = "Project Zero";
	public static final int GAME_WIDTH = 320;
	public static int GAME_HEIGHT;
	public static float RATIO;
	
	// declare all screens
	public static SplashScreen splashScreen;
	public static MainScreen mainScreen;
	public static HelpScreen helpScreen;
	public static RoomScreen roomScreen;
	public static SelectionScreen selectionScreen;
//	public static GameScreen gameScreen;
//	public static MenuScreen menuScreen;
	
	@Override
	public void create () {
		// declare variables
		RATIO = (float) GAME_WIDTH / Gdx.graphics.getWidth();
		
		// declare screens
		splashScreen = new SplashScreen(this);
		mainScreen = new MainScreen(this);
		helpScreen = new HelpScreen(this);
		roomScreen = new RoomScreen(this);
		selectionScreen = new SelectionScreen(this);
//		gameScreen = new GameScreen(this);
//		menuScreen = new MenuScreen(this);
		
		// load all assets
		AssetHandler.load();
		
		// proceed to splash screen
		setScreen(splashScreen);
	}
	
	public void dispose() {
		// release all assets
		AssetHandler.release();
	}
}
