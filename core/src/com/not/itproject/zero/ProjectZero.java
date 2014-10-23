package com.not.itproject.zero;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.not.itproject.handlers.AssetHandler;
import com.not.itproject.handlers.NetworkHandler;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.screens.AbstractScreen;
import com.not.itproject.screens.ErrorScreen;
import com.not.itproject.screens.GameScreen;
import com.not.itproject.screens.HelpScreen;
import com.not.itproject.screens.MainScreen;
import com.not.itproject.screens.RoomScreen;
import com.not.itproject.screens.SelectionScreen;
import com.not.itproject.screens.SplashScreen;
import com.not.itproject.sessions.GameSession;

public class ProjectZero extends Game {
	// declare variables
	public static final String GAME_NAME = "Project Zero";
	public static final int GAME_WIDTH = 320;
	public static int GAME_HEIGHT;
	public static float RATIO;
	public static Calendar calendar; 
	
	// declare all screens
	public static SplashScreen splashScreen;
	public static MainScreen mainScreen;
	public static HelpScreen helpScreen;
	public static RoomScreen roomScreen;
	public static SelectionScreen selectionScreen;
	public static GameScreen gameScreen;
	public static ErrorScreen errorScreen;
	public static AbstractScreen previousScreen;

	// declare game session
	public static GameSession gameSession;
	
	@Override
	public void create () {
		// declare variables
		RATIO = (float) GAME_WIDTH / Gdx.graphics.getWidth();
		calendar = new GregorianCalendar();

		// load all assets
		AssetHandler.load();

		// initialise ProjectZero		
		initialise();
		
		// proceed to splash screen
		setScreen(splashScreen);
	}
	
	public void initialise() {
		// initialise game session
		gameSession = new GameSession();
		
		// load network
		NetworkHandler.load();
		
		// declare screens
		splashScreen = new SplashScreen(this);
		mainScreen = new MainScreen(this);
		helpScreen = new HelpScreen(this);
		roomScreen = new RoomScreen(this);
		selectionScreen = new SelectionScreen(this);
		gameScreen = new GameScreen(this);
		errorScreen = new ErrorScreen(this);
	}
	
	public void nextScreen(AbstractScreen next, AbstractScreen prev) {
		// record prev screen
		previousScreen = prev;
		
		// proceed to next screen
		setScreen(next);
	}
	
	public static void log(String message) {
		// log message into console
		if (GameVariables.DEBUG) Gdx.app.log(GAME_NAME, message);
	}
	
	public void dispose() {
		// release all assets
		AssetHandler.release();
	}
}
